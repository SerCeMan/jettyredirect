import org.eclipse.jetty.server.*
import org.eclipse.jetty.servlet.*
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import java.util.concurrent.CompletableFuture

@Controller("/")
open class Page {
    @GetMapping("/page1")
    open fun page1(str: String) = CompletableFuture.completedFuture("forward:/page2")

    @GetMapping("/page2")
    @ResponseBody
    open fun page2(str: String) = "params: $str"
}

fun main() {
    Server().apply {
        insertHandler(ServletContextHandler().apply {
            val ctx = AnnotationConfigWebApplicationContext().apply {
                register(Page::class.java)
                refresh()
            }
            addServlet(ServletHolder(DispatcherServlet(ctx)), "/")
            servletContext.setAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx)
        })
        addConnector(ServerConnector(server).apply { port = 8070 })
        start()
        join()
    }
}