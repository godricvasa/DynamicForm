    package main.webapp.Functionality;

    import com.google.gson.Gson;
    import com.google.gson.JsonArray;
    import com.google.gson.JsonObject;


    import javax.servlet.ServletException;
    import javax.servlet.annotation.WebServlet;
    import javax.servlet.http.HttpServlet;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.FileReader;
    import java.io.IOException;

    @WebServlet("/FormMetadataServlet")
    public class FormMetadataServlet extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

//            logger.info("Call received");
            String tableName = request.getParameter("tableName");
            String filePath = getServletContext().getRealPath("/WEB-INF/form_metadata.json");

    //            System.out.println(obj);
            try (FileReader reader = new FileReader(filePath)) {

//                logger.finer("reader can read the json file");


                JsonObject metadata = new Gson().fromJson(reader, JsonObject.class);
    //            System.out.println(metadata.toString());


                if (metadata.has(tableName)) {
//                    logger.finer("the table name exists in the metadata");
                    JsonArray tableMetadata = metadata.getAsJsonArray(tableName);
    //                System.out.println(tableMetadata.toString());
                    response.getWriter().write(tableMetadata.toString());
                } else {
//                    logger.warning("the table does not exitst in the json file");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Invalid table name\"}");
                }
            } catch (IOException e) {
//                logger.severe("there is a IO exception");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Error reading metadata file\"}");
            }
        }


    }

