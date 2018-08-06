package com.castinfo.devops.robotest.examples;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.castinfo.devops.robotest.RobotestException;
import com.castinfo.devops.robotest.TestCase;
import com.castinfo.devops.robotest.annot.RobotestCase;
import com.castinfo.devops.robotest.annot.RobotestSuite;
import com.castinfo.devops.robotest.junit.JUnitCaseRunner;

/**
 * Robotest Shop Suite that runs a series of tests of Cast-info web page
 *
 *
 */
@RobotestSuite(tag = "CASTINFO_SUITE_001", description = "Standad navigetion through Cast-Info page")
@RunWith(JUnitCaseRunner.class)
public class ITCastExamples extends TestCase {

    /**
     * Checks Home Page title
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "HOME_CASE_001", description = "Home Case Check Title")
    public void checkHomeTitle() throws RobotestException {
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preHome.openURL("http://www.cast-info.es");
        preHome.waitForPageLoaded(1L);
        preHome.checkTitle();
    }

    /**
     * Checks Home page's access to Contacto form ant fills its content
     *
     * @throws RobotestException
     * @throws XPathExpressionException
     * @throws RobotestApiTestingException
     * @throws XPathExpressionException
     */
    @Test
    @RobotestCase(tag = "HOME_CASE_002", description = "Home Case Check Form Contacto")
    public void checkHomeContacto() throws RobotestException {
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preHome.openURL("http://www.cast-info.es");
        preHome.waitForPageLoaded(1L);
        preHome.checkSearchContacto();
        preHome.checkGoContacto();
        preHome.waitForPageLoaded(1L);
        preHome.checkFormExists();
        preHome.checkUseForm();
    }

    /**
     * Checks Home page top menu items
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "HOME_CASE_003", description = "Home Case Check top-menu-items")
    public void checkHomeTopMenuItems() throws RobotestException {
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preHome.openURL("http://www.cast-info.es");
        preHome.waitForPageLoaded(1L);
        preHome.checkTopMenu();
        preHome.checkTopMenuLinks();
    }

    /**
     * Checks Home page Links URL equals resulting page
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "HOME_CASE_004", description = "Home Case Check all links")
    public void checkHomeInternalLinks() throws RobotestException {
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preHome.openURL("http://www.cast-info.es");
        preHome.waitForPageLoaded(1L);
        preHome.checkAllLinks();
    }

    /**
     * Checks Home page Slider buttons
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "HOME_CASE_005", description = "Home Case Slider Controls")
    public void checkHomeSliderControls() throws RobotestException {
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preHome.openURL("http://www.cast-info.es");
        preHome.getDriver()
               .manage()
               .window()
               .maximize();
        preHome.checkSliderButtons();
    }

    /**
     * Checks Home page Cookies
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "HOME_CASE_006", description = "Home Case Cookies")
    public void checkHomeCookies() throws RobotestException {
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preHome.openURL("http://www.cast-info.es");
        preHome.waitForPageLoaded(1L);
        preHome.checkCastCookies();
        preHome.checkCreateCookie();
        preHome.checkDeleteCastCookies();
    }

    /**
     * Checks Empresa page title coincidence with literal expression
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "EMPRESA_CASE_001", description = "Empresa Case Title")
    public void checkEmpTitle() throws RobotestException {
        EmpresaPageObject preEmp = this.buildPageObject(EmpresaPageObject.class);
        preEmp.openURL("https://www.cast-info.es/empresa/");
        preEmp.waitForPageLoaded(1L);
        preEmp.checkTitle();
    }

    /**
     * Check Empresa's page inner secionts
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "EMPRESA_CASE_002", description = "Empresa Case Sections")
    public void checkEmpSections() throws RobotestException {
        EmpresaPageObject preEmp = this.buildPageObject(EmpresaPageObject.class);
        preEmp.openURL("https://www.cast-info.es/empresa/");
        preEmp.waitForPageLoaded(1L);
        preEmp.checkSections();
    }

    /**
     * Check Empresa page Calidad links
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "EMPRESA_CASE_003", description = "Empresa Case Calidad Links")
    public void checkEmpCalidad() throws RobotestException {
        EmpresaPageObject preEmp = this.buildPageObject(EmpresaPageObject.class);
        preEmp.openURL("https://www.cast-info.es/empresa/");
        preEmp.waitForPageLoaded(1L);
        preEmp.checkCalidadLinks();
    }

    /**
     * Check Empresa page Clientes links
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "EMPRESA_CASE_004", description = "Empresa Case Clients")
    public void checkEmpClients() throws RobotestException {
        EmpresaPageObject preEmp = this.buildPageObject(EmpresaPageObject.class);
        preEmp.openURL("https://www.cast-info.es/empresa/");
        preEmp.waitForPageLoaded(1L);
        preEmp.checkClientesArrows();
        preEmp.checkClientesLinks();
    }

    /**
     * Check Empresa Google's Maps Frame Pins
     *
     * @throws RobotestException
     * @throws InterruptedException
     */
    @Test
    @RobotestCase(tag = "EMPRESA_CASE_005", description = "Empresa Case Google Maps")
    public void checkEmpGoogleMaps() throws RobotestException, InterruptedException {
        EmpresaPageObject preEmp = this.buildPageObject(EmpresaPageObject.class);
        preEmp.openURL("https://www.cast-info.es/empresa/");
        preEmp.waitForPageLoaded(1L);
        preEmp.checkMaps();
    }

    /**
     * Check Soluciones page inner links
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "SOLUCIONES_CASE_001", description = "Soluciones Case Link List")
    public void checkSolucionesLinkList() throws RobotestException {
        SolucionesPageObject preSol = this.buildPageObject(SolucionesPageObject.class);
        preSol.openURL("https://www.cast-info.es/soluciones/");
        preSol.waitForPageLoaded(1L);
        preSol.checkLinkList();
    }

    /**
     * Check Clientes page access to contacto form an fill in
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "CLIENTES_CASE_001", description = "Clientes Case Link List")
    public void checkClientesContacto() throws RobotestException {
        ClientesPageObject preCli = this.buildPageObject(ClientesPageObject.class);
        preCli.openURL("https://www.cast-info.es/clientes/");
        preCli.waitForPageLoaded(1L);
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preCli.checkContacto(preHome);
    }

    /**
     * Check Clientes page inner links
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "CLIENTES_CASE_002", description = "Clientes Case Link List")
    public void checkClientesLinks() throws RobotestException {
        ClientesPageObject preCli = this.buildPageObject(ClientesPageObject.class);
        preCli.openURL("https://www.cast-info.es/clientes/");
        preCli.waitForPageLoaded(1L);
        preCli.checkClientesLinks();
    }

    /**
     * Check Empleo page links related to Delegacion inner section
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "EMPLEO_CASE_001", description = "Empleo Case Delegacion")
    public void checkEmpleoDelegacion() throws RobotestException {
        EmpleoPageObject preEmp = this.buildPageObject(EmpleoPageObject.class);
        preEmp.openURL("https://www.cast-info.es/empleo/");
        preEmp.waitForPageLoaded(1L);
        preEmp.checkEmpleoDelegacion();
    }

    /**
     * Check Empleo page eMail buttons
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "EMPLEO_CASE_002", description = "Empleo Case eMail")
    public void checkEmpleoEmail() throws RobotestException {
        EmpleoPageObject preEmp = this.buildPageObject(EmpleoPageObject.class);
        preEmp.openURL("https://www.cast-info.es/empleo/");
        preEmp.waitForPageLoaded(1L);
        HomePageObject preHome = this.buildPageObject(HomePageObject.class);
        preEmp.checkEmpleoEmail(preHome);
    }

    /**
     * Check Noticias page Post links
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "NOTICIAS_CASE_001", description = "Noticias Case Post")
    public void checkNoticiasPost() throws RobotestException {
        NoticiasPageObject preNot = this.buildPageObject(NoticiasPageObject.class);
        preNot.openURL("https://www.cast-info.es/noticias/");
        preNot.waitForPageLoaded(1L);
        preNot.checkNoticiasPost();
    }

    /**
     * Check Noticias page search form and performa search action
     *
     * @throws RobotestException
     */
    @Test
    @RobotestCase(tag = "NOTICIAS_CASE_001", description = "Noticias Case Search")
    public void checkNoticiasSearch() throws RobotestException {
        NoticiasPageObject preNot = this.buildPageObject(NoticiasPageObject.class);
        preNot.openURL("https://www.cast-info.es/noticias/");
        preNot.waitForPageLoaded(1L);
        preNot.checkNoticiasSearch();
    }
}