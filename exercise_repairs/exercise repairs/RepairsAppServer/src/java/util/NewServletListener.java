/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author juanluis
 */
public class NewServletListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("context created");
    ServletContext context = sce.getServletContext();
    Global global = new Global();
    context.setAttribute("global", global);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    System.out.println("context destroyed");
    ServletContext context = sce.getServletContext();
    Global global = (Global)context.getAttribute("global");
    global.storeData();
  }
}
