package org.enricogiurin.webserver;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class WebServerTest {

    private static final int SERVER_PORT = 8082;

    private Server server;
    private Thread thread;
    private static String root;
    private WebClient webClient;

    @BeforeClass
    public static void beforeClass() {
        ClassLoader classLoader = WebServerTest.class.getClassLoader();
        File file = new File(classLoader.getResource("web").getFile());
        root = file.getAbsolutePath();
    }

    @Before
    public void setUp() {
        server = new Server(root, String.valueOf(SERVER_PORT));
        thread = new Thread(server);
        thread.start();
        webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(false);
    }

    @After
    public void tearDown() {
        try {
            server.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldGET_OK() throws Exception {
        Page page = webClient.getPage("http://127.0.0.1:" + SERVER_PORT + "/index.html");
        assertThat(page.getWebResponse().getStatusCode()).isEqualTo(200);
        assertThat(page.getWebResponse().getContentType()).isEqualTo("text/html");
    }

    @Test
    public void shouldGET_404() throws Exception {
        Page page = webClient.getPage("http://127.0.0.1:" + SERVER_PORT + "/notAFile.html");
        assertThat(page.getWebResponse().getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldGET_403() throws Exception {
        Page page = webClient.getPage("http://127.0.0.1:" + SERVER_PORT);
        assertThat(page.getWebResponse().getStatusCode()).isEqualTo(403);
    }

}
