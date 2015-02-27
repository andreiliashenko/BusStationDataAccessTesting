package com.anli.busstation.dal.test.servlet;

import com.anli.integrationtesting.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractTester extends HttpServlet {

    protected static final String TEMPLATE_PATH = "/template.html";
    protected static final String TESTS_EXPRESSION = "${tests}";
    protected static final String URL_EXPRESSION = "${url}";
    protected static final String TITLE_EXPRESSION = "${title}";

    protected static final List<String> vehiclesTests = Arrays.asList("GasLabelTest", "TechnicalStateTest",
            "ModelTest", "BusTest");
    protected static final List<String> staffTests = Arrays.asList("DriverSkillTest", "MechanicSkillTest",
            "SalesmanTest", "DriverTest", "MechanicTest", "EmployeeTest");
    protected static final List<String> geographyTests = Arrays.asList("StationTest", "RegionTest", "RoadTest");
    protected static final List<String> maintenanceTests = Arrays.asList("BusRepairmentTest",
            "BusRefuellingTest", "BusServiceTest", "StationServiceTest", "TechnicalAssignmentTest");
    protected static final List<String> trafficTests = Arrays.asList("RoutePointTest", "RidePointTest", "RideRoadTest",
            "TicketTest");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String resultHtml = getHtml(request.getServletContext());
        out.print(resultHtml);
    }

    protected String getHtml(ServletContext context) throws IOException {
        String template = getTemplateHtml(context);
        return getEvaluatedHtml(template, getTestList(), getExecutorUrl(), getTitle());
    }

    protected String getTestList() {
        ArrayList<Test> testList = new ArrayList<>();
        for (String vehiclesTest : vehiclesTests) {
            testList.add(new Test(vehiclesTest, getBasePackage() + ".vehicles." + vehiclesTest));
        }
        for (String staffTest : staffTests) {
            testList.add(new Test(staffTest, getBasePackage() + ".staff." + staffTest));
        }
        for (String geographyTest : geographyTests) {
            testList.add(new Test(geographyTest, getBasePackage() + ".geography." + geographyTest));
        }
        for (String maintenanceTest : maintenanceTests) {
            testList.add(new Test(maintenanceTest, getBasePackage() + ".maintenance." + maintenanceTest));
        }
        for (String trafficTest : trafficTests) {
            testList.add(new Test(trafficTest, getBasePackage() + ".traffic." + trafficTest));
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        boolean isFirst = true;
        for (Test test : testList) {
            if (!isFirst) {
                builder.append(", ");
            } else {
                isFirst = false;
            }
            builder.append(test.toJson());
        }
        return builder.append("]").toString();
    }

    protected String getTemplateHtml(ServletContext context) throws IOException {
        InputStream templateStream = context.getResourceAsStream(TEMPLATE_PATH);
        BufferedReader templateReader = new BufferedReader(new InputStreamReader(templateStream));
        StringBuilder templateHtml = new StringBuilder();
        String line = templateReader.readLine();
        while (line != null) {
            templateHtml.append(line).append("\n");
            line = templateReader.readLine();
        }
        return templateHtml.toString();
    }

    protected String getEvaluatedHtml(String template, String tests, String url, String title) {
        return template.replace(TESTS_EXPRESSION, tests)
                .replace(URL_EXPRESSION, url)
                .replace(TITLE_EXPRESSION, title);
    }

    protected abstract String getBasePackage();

    protected abstract String getExecutorUrl();

    protected abstract String getTitle();
}
