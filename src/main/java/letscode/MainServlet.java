package letscode;

import com.sun.tools.javac.util.StringUtils;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/cool-servlet", "/my-cool-servlet/*"})
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        String params = formatParams(req);
        resp.getWriter().write("Method doGet\nURI: " + uri + "\nParams:\n" + params + "\n");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String params = formatParams(req);
        params = params.substring(5);

        FileInputStream fis = new FileInputStream(new File("E:/OOP/text.txt"));
        byte[] content = new byte[fis.available()];
        fis.read(content);
        fis.close();
        String[] lines = new String(content, "UTF-8").split(("\n"));
        List<String> list = new ArrayList<String>();
        int i = 1;
        int count = 0;
        for (String line : lines) {
            line = line.replaceAll("\\p{Punct}","");
            String[] words = line.split(" ");
            int j = 1;
            for (String word : words) {
                if (word.equalsIgnoreCase(params)) {
                    list.add("Cлово " +word + " найдено в " + i + "-й строке, " + j + "-е слово");
                    count = count+1;
                }
                j++;
            }
            i++;
        }
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Количество совпадений</h2>");
        out.println("<h2>"+ count + "</h2>");
        for (int k = 0; k< list.size();k++){
            out.println("<p>"+list.get(k)+"</p>");
        }
        out.println("</body></html>");
    }
    private String formatParams(HttpServletRequest req) {
        return req.getParameterMap()
                .entrySet()
                .stream()
                .map(entry -> {
                    String param = String.join(" and ", entry.getValue());
                    return entry.getKey() + param;
                })
                .collect(Collectors.joining("\n"));
    }

}
