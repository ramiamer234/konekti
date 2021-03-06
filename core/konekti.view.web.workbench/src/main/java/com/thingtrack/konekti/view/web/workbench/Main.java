/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.thingtrack.konekti.view.web.workbench;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.dellroad.stuff.vaadin.SpringContextApplication;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.ResourceBundleI18N;
import com.thingtrack.konekti.domain.Action;
import com.thingtrack.konekti.domain.Configuration;
import com.thingtrack.konekti.domain.MenuCommandResource;
import com.thingtrack.konekti.domain.MenuFolderResource;
import com.thingtrack.konekti.domain.MenuResource;
import com.thingtrack.konekti.domain.MenuWorkbench;
import com.thingtrack.konekti.domain.Permission;
import com.thingtrack.konekti.domain.Role;
import com.thingtrack.konekti.domain.User;
import com.thingtrack.konekti.service.api.AreaService;
import com.thingtrack.konekti.service.api.ConfigurationService;
import com.thingtrack.konekti.service.api.LocationService;
import com.thingtrack.konekti.service.api.MenuWorkbenchService;
import com.thingtrack.konekti.service.api.OrganizationService;
import com.thingtrack.konekti.service.api.UserService;
import com.thingtrack.konekti.service.security.SecurityService;
import com.thingtrack.konekti.view.addon.ui.SliderView;
import com.thingtrack.konekti.view.kernel.IMetadataModuleServiceListener;
import com.thingtrack.konekti.view.kernel.IModuleService;
import com.thingtrack.konekti.view.kernel.IWorkbenchContext;
import com.thingtrack.konekti.view.kernel.MetadataModule;
import com.thingtrack.konekti.view.kernel.ui.layout.IApplicationCloseEventListener;
import com.thingtrack.konekti.view.kernel.ui.layout.IUserChangeListener;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewChangeListener;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewContainer;
import com.thingtrack.konekti.view.kernel.ui.layout.LOCATION;
import com.thingtrack.konekti.view.kernel.ui.layout.ViewEvent;
import com.thingtrack.konekti.view.layout.KonektiLayout;
import com.thingtrack.konekti.view.web.workbench.ui.MenuManager;
import com.thingtrack.konekti.view.web.workbench.ui.ResourceManager;
import com.thingtrack.konekti.view.web.workbench.ui.ResourceManager.Resource;
import com.thingtrack.konekti.view.web.workbench.ui.ToolbarManager;
import com.thingtrack.konekti.view.web.workbench.ui.WorkbenchContext;
import com.thingtrack.konekti.view.web.form.field.LocaleField;
import com.thingtrack.konekti.view.addon.ui.ErrorViewForm;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class Main extends SpringContextApplication implements IMetadataModuleServiceListener, IViewChangeListener, IUserChangeListener {
	private final static Logger logger = Logger.getLogger(SpringContextApplication.class.getName());
    
	@Autowired
	private IModuleService moduleService;

	@Autowired
	private MenuWorkbenchService menuWorkbenchService;
	
	@Autowired
	private SecurityService securityService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private AreaService areaService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private ResourceManager resourceManager;

	@Autowired
	private ToolbarManager toolbarManager;

	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	private KonektiLayout konektiLayout;

	private MainWindow window;
	
	private SliderView sliderView;

	private IWorkbenchContext workbenchContext;
	
	private String name;
	private String version;
	private String logoInit;
	private boolean demo;
	private boolean jira;
	private String jiraURL;
	
	private static int DEFAULT_MENU_ID = 0;
	private MenuItem headMenuItem;
	
	private I18N i18n;
	
	@Override
	protected void initSpringApplication(ConfigurableWebApplicationContext context) {
		// set konekti theme
		setTheme("konekti");
						
		// get global konekti configuration
		getConfiguration();
		
		// set main Window
		window = new MainWindow(name, configureI18n());
		
		window.setStyleName("background");
		setMainWindow(window);

		// set full size and none margin for the default window layout
		VerticalLayout mainLayout = (VerticalLayout) window.getContent();
		mainLayout.setSizeFull();
		mainLayout.setMargin(false);
		
		// Create the views
		createViews();

		// add Konekti Layout
		mainLayout.addComponent(sliderView);

		// add module listener to list bundle install/uninstall
		moduleService.addListener(this);
		
		// add user change listener
		konektiLayout.getMenuLayout().addListenerUserChange(this);
		
		// set jira issue collector button
		generateJIRAIssueCollector();		
				
		// define MenuLayout Message listeners
		konektiLayout.getMenuLayout().addListenerApplicationClose(new IApplicationCloseEventListener() {			
			@Override
			public void close() {
				moduleService.removeListener(Main.this);
														
				WebApplicationContext webApplicationContext = (WebApplicationContext) getMainWindow().getApplication().getContext();
				webApplicationContext.getHttpSession().invalidate();
				
				getMainWindow().getApplication().close();
				
			}
		});
		
	}
	
	// Overriding getWindow(String) is required to get multiple browser windows/tabs to work
	/*@Override
	public Window getWindow(String name) {
		// If the window is identified by name, we are good to go
		Window w = super.getWindow(name);
		// If not, we must create a new window for this new browser window/tab
		if (w == null) {			
			//w = new CalcWindow();
			w = new MainWindow(name, configureI18n());
			
			// Use the random name given by the framework to identify this
			// window in future
			w.setName(name);
			addWindow(w);
			// Move to the url to remember the name in the future
			w.open(new ExternalResource(w.getURL()));
		}
		return w;
	}*/
		
	private I18N configureI18n() {				
		// set locales supported by Konekti
		Locale esLocale = new Locale("es");
		Locale enLocale = new Locale("en");
		Locale frLocale = new Locale("fr");
		Locale zhLocale = new Locale("zh");
		
		// configure locale sources
		i18n = new ResourceBundleI18N("com/thingtrack/konekti/view/web/i18n/messages", getBundleClassLoader(), esLocale, enLocale, frLocale, zhLocale);

		// set default locale from browser configuration
		if (getLocale().toString().contains("es"))
			i18n.setCurrentLocale(esLocale);
		else if (getLocale().toString().contains("en"))
			i18n.setCurrentLocale(enLocale);
		else if (getLocale().toString().contains("fr"))
			i18n.setCurrentLocale(frLocale);
		else if (getLocale().toString().contains("zh"))
			i18n.setCurrentLocale(zhLocale);
		else
			i18n.setCurrentLocale(esLocale);
		
		return i18n;
	}

	private void generateJIRAIssueCollector() {
		// set jira issue collector button
		if (jira) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("jQuery.ajax({");
			buffer.append("url: \"" + jiraURL + "\"");
			buffer.append(",type: \"get\"");
			buffer.append(",cache: true");
			buffer.append(",dataType: \"script\"});");
			getMainWindow().executeJavaScript(buffer.toString()); 
		}
	}
	
	private ClassLoader getBundleClassLoader() {
		BundleContext bundleContext = FrameworkUtil.getBundle(Main.class).getBundleContext();
		
		Bundle bundle = bundleContext.getBundle();
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		ClassLoader classLoader = bundleWiring.getClassLoader();
		
		return classLoader;
	}
	
	private void getConfiguration() {
		Configuration configuration = null;
		try {
			configuration = configurationService.getByTag(Configuration.TAG.NAME.name());
			name = configuration.getValue();
		} catch (Exception e) {
			name = "KONEKTI";
		}	
		
		try {
			configuration = configurationService.getByTag(Configuration.TAG.VERSION.name());
			version = configuration.getValue();
		} catch (Exception e) {
			version = "0.0.1.SNAPSHOT";
		}	
		
		try {
			configuration = configurationService.getByTag(Configuration.TAG.LOGO_INIT.name());
			logoInit = configuration.getValue();
		} catch (Exception e) {
			logoInit = "logo_konekti_init.png";
		}	
		
		try {
			configuration = configurationService.getByTag(Configuration.TAG.DEMO.name());
			demo = Boolean.parseBoolean(configuration.getValue());
		} catch (Exception e) {
			demo = false;
		}
		
		try {
			configuration = configurationService.getByTag("JIRA");
			jira = Boolean.parseBoolean(configuration.getValue());
		} catch (Exception e) {
			jira = false;
		}
		
		try {
			configuration = configurationService.getByTag("JIRA_URL");
			jiraURL = configuration.getValue();
		} catch (Exception e) {
			// konekti issueCollector
			jiraURL = "https://thingtrack.atlassian.net/s/6791a7bea4f8d576fd0a266972dba049-T/es_ES-nr1nwy/64001/71/1.4.13/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?locale=es-ES&collectorId=5e180215";
		}
			
	}
	
	private void createViews() {
		sliderView = new SliderView();

		sliderView.addListener(this);

		sliderView.addView(new SecurityAccessView(securityService, userService, sliderView, version, logoInit, demo));		
		sliderView.addView(new WorkbenchView(konektiLayout, sliderView));
	}

	protected void initMenuManager(User user) {
		try {
			// STEP 1: construct vertical menu
			List<MenuWorkbench> menuResources = menuWorkbenchService.getAll();

			// get the first menu
			MenuWorkbench menuDefault = menuResources.get(DEFAULT_MENU_ID);

			for (MenuFolderResource menuFolderResource : menuDefault.getMenuFolderResource()) {
				// add new menu item
				headMenuItem = konektiLayout.getMenuLayout()
						.addMenuItem(
								i18n.getMessage(menuFolderResource.getKeyCaption()),
								getIcon(menuFolderResource.getIcon(),
										menuFolderResource.getCaption()), null);

				// recursive menu manage
				getMenu(menuFolderResource, headMenuItem, user);
				
				// remove menu items if don't have any command
				if (!headMenuItem.hasChildren())
					konektiLayout.getMenuLayout().removeItem(headMenuItem);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	private void getMenu(MenuFolderResource menuFolderResource, MenuItem itemParentId, User user) {
		for (final MenuResource menuResource : menuFolderResource.getMenuResources()) {
			if (menuResource instanceof MenuFolderResource) {
				// add new submenu item
				MenuItem headMenuResourceItem = konektiLayout.getMenuLayout().addMenuItem(
							i18n.getMessage(menuFolderResource.getKeyCaption()),
							getIcon(menuResource.getIcon(),menuResource.getCaption()),
							itemParentId, null);

				// recursive menu manage
				getMenu((MenuFolderResource) menuResource, headMenuResourceItem, user);
				
				// remove submenu items if don't have any command
				if (!headMenuResourceItem.hasChildren())
					itemParentId.removeChild(headMenuResourceItem);
				
			} else {
				if (((MenuCommandResource) menuResource).getType() == MenuCommandResource.TYPE.SEPARATOR)
					itemParentId.addSeparator();
				else {
					// check if exist VIEW permission for each module for the active user role
					if (getCommandPermission(user, (MenuCommandResource) menuResource)) {
						// default menu command
						Command defaultCommand = new Command() {
							@Override
							public void menuSelected(MenuItem selectedItem) {
								window.showNotification(
										"Module Warning",
										"The Module: "
												+ ((MenuCommandResource) menuResource)
														.getModuleId()
												+ "["
												+ ((MenuCommandResource) menuResource)
														.getModuleVersion()
												+ "] is not deploy",
										Window.Notification.TYPE_WARNING_MESSAGE);
	
							}
						};
	
						// create the new menu item; recover commanda data
						String symbolicName = ((MenuCommandResource) menuResource).getModuleId();
						String version = ((MenuCommandResource) menuResource).getModuleVersion();
	
						String id = symbolicName + "#" + version;						
						String caption = i18n.getMessage(((MenuCommandResource) menuResource).getKeyCaption());
						String hint = i18n.getMessage(((MenuCommandResource) menuResource).getKeyHint());
						
						boolean autoStart = ((MenuCommandResource) menuResource).isAutostart();
						boolean closeable = ((MenuCommandResource) menuResource).isClosable();
						
						com.vaadin.terminal.Resource resource = null;
						if (((MenuCommandResource) menuResource) != null)
							resource = getIcon(((MenuCommandResource) menuResource).getIcon(), caption);
	
						// create the new menu item
						MenuItem headMenuItem = konektiLayout.getMenuLayout().addMenuItem(caption, hint, resource, itemParentId, defaultCommand);
	
						// add command to the menu manager list
						if (((MenuCommandResource) menuResource).getLocation().name().equals("TOP"))
							resourceManager.addResource(id, caption, resource, LOCATION.TOP, autoStart, closeable, null, headMenuItem, null);
						else if (((MenuCommandResource) menuResource).getLocation().name().equals("LEFT"))
							resourceManager.addResource(id, caption, resource, LOCATION.LEFT, autoStart, closeable, null, headMenuItem, null);
						else if (((MenuCommandResource) menuResource).getLocation().name().equals("CENTER"))
							resourceManager.addResource(id, caption, resource, LOCATION.CENTER, autoStart, closeable, null, headMenuItem, null);
						else if (((MenuCommandResource) menuResource).getLocation().name().equals("RIGHT"))
							resourceManager.addResource(id, caption, resource, LOCATION.RIGHT, autoStart, closeable, null, headMenuItem, null);
						else if (((MenuCommandResource) menuResource).getLocation().name().equals("BOTTON"))
							resourceManager.addResource(id, caption, resource, LOCATION.BOTTON, autoStart, closeable, null, headMenuItem, null);
	
						// set module payload if is register in service registry
						MetadataModule metadataModule = moduleService.get(symbolicName, version);
	
						if (metadataModule != null) {
							try {
								setModuleResource(metadataModule);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
		
	private StreamResource getIcon(final byte[] byteResource, String name) {
		if (byteResource == null)
			return null;

		// create an instance of our stream source.
		StreamSource imagesource = new ModuleResource(byteResource);

		// Create a resource that uses the stream source and give it a name.
		// The constructor will automatically register the resource in the
		// application.
		StreamResource imageresource = new StreamResource(imagesource, name
				+ ".png", this);

		return imageresource;
	}

	private class ModuleResource implements StreamResource.StreamSource {
		private byte[] resource = null;
		private ByteArrayOutputStream imagebuffer = null;

		public ModuleResource(byte[] resource) {
			this.resource = resource;

		}

		@Override
		public InputStream getStream() {
			if (resource != null) {
				InputStream in = new ByteArrayInputStream(resource);
				BufferedImage bImageFromConvert = null;

				try {
					bImageFromConvert = ImageIO.read(in);
					imagebuffer = new ByteArrayOutputStream();

					ImageIO.write(bImageFromConvert, "png", imagebuffer);
				} catch (IOException e) {
					e.printStackTrace();

				}

				return new ByteArrayInputStream(imagebuffer.toByteArray());
			}

			return null;
		}

	}

	private void setModuleResource(MetadataModule metadataModule) throws Exception {
		final Resource resource = resourceManager.getResource(metadataModule.getId() + "#" + metadataModule.getVersion());

		if (resource != null) {
			IViewContainer viewContainer = metadataModule.getModule().createViewComponent(workbenchContext);
						
			viewContainer.addListener((IViewChangeListener) toolbarManager);
			
			resource.setComponentView(viewContainer);

			// set command (handler) to item menu
			resource.getMenu().setCommand(new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					konektiLayout.addModule(resource.getId(),
							resource.getCaption(), resource.getComponentView(), resource.isCloseable(),
							resource.getResource(), resource.getLocation());

				}
			});

			// autostart if the flag is active
			if (resource.isAutoStart())
				konektiLayout.addModule(resource.getId(),
						resource.getCaption(), resource.getComponentView(), resource.isCloseable(),
						resource.getResource(), resource.getLocation());
		}
	}

	private Boolean getCommandPermission(User user, MenuCommandResource command) {
		for  (Role role : user.getRoles()) {
			if (role.getArea().equals(user.getActiveArea())) {
				for (Permission permission : role.getPermissions()) {
					if (permission.getMenuCommandResource() != null &&
						permission.getMenuCommandResource().getMenuResourceId() == command.getMenuResourceId()) {
						for (Action action : permission.getActions()) {
							if (action.getCode().equals(Action.ACTION.VIEW.name()) && action.isActive())
								return permission.isActive();
						}
					}
					
				}
			}
		}
		
		return false;
	}
	
	@Override
	public void metadataModuleRegistered(IModuleService source, MetadataModule metadataModule) {
		try {
			setModuleResource(metadataModule);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void metadataModuleUnregistered(IModuleService source, final MetadataModule metadataModule) {
		String id = metadataModule.getId() + "#" + metadataModule.getVersion();

		Resource resource = resourceManager.getResource(id);

		if (resource != null) {
			// remove the bundle tab resource if it's opened
			try {
				konektiLayout.removeModule(resource.getId());
				resource.setTab(null);
			} catch (Exception e) {
				e.getMessage();
			}

			// set default command to the bundle menu resource
			Command defaultCommand = new Command() {
				@Override
				public void menuSelected(MenuItem selectedItem) {
					window.showNotification("Module Warning",
							"The Module: " + metadataModule.getId() + "["
									+ metadataModule.getVersion()
									+ "] is not deploy",
							Window.Notification.TYPE_WARNING_MESSAGE);

				}
			};
			resource.getMenu().setCommand(defaultCommand);

			// reset the bundle component resource
			resource.setComponentView(null);

		}

	}

	@Override
	public void viewChanged(ViewEvent viewEvent) {
		if (viewEvent.getViewFrom() instanceof SecurityAccessView) {

			SecurityAccessView securityAccessView = (SecurityAccessView) viewEvent.getViewFrom();
			try {								
				User loggedUser = userService.getByUsername(securityAccessView.getGrantedUser().getUsername());
				loadWorkbenchContext(loggedUser);
				
				WorkbenchView workbenchView =  (WorkbenchView) viewEvent.getViewTo();
				workbenchView.setLoggedUser(loggedUser);
								
				menuManager.setUser(loggedUser);				
				menuManager.setContext(workbenchContext);
				
				// construct the menu from scratch
				initMenuManager(loggedUser);
			} catch (Exception e) {
				throw new RuntimeException(e);
				
			}
		}

		if (viewEvent.getViewFrom() instanceof WorkbenchView) {

		}
	}

	private void loadWorkbenchContext(User user) throws Exception {	
		// create locale for employee agent
		String[] localeParams = user.getDefaultLocale().split(LocaleField.LOCALE_SEPARATOR);
			
		String language = localeParams[0];
		String country = localeParams[1];
		
		//defaultLocale = new Locale(language, country);
		Locale defaultLocale = new Locale(language);
		
		// initialize active Organization tree
		user.setActiveOrganization(user.getDefaultOrganization());
		user.setActiveLocation(user.getDefaultLocation());
		user.setActiveArea(user.getDefaultArea());
		user.setActiveLocale(defaultLocale);
		
		// set user Locale		
		i18n.setCurrentLocale(defaultLocale);		
		
		// get all configuratios
		List<Configuration> configuration = configurationService.getAll(user);				
			workbenchContext = new WorkbenchContext(user,
													configuration,
													menuManager,
													toolbarManager,
													resourceManager);
	}
	
	@Override
	public void terminalError(Terminal.ErrorEvent event) {
	    final Throwable t = event.getThrowable();
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            logger.info("SocketException in CommunicationManager." + " Most likely client (browser) closed socket.");
            
            return;
        }
        
        // show default error view form
        ErrorViewForm errorViewForm;
        if (t.getCause() != null)
        	errorViewForm = new ErrorViewForm(t.getCause().getMessage(), t.getCause().getCause(), getMainWindow());
        else
        	errorViewForm = new ErrorViewForm(null, null, getMainWindow());
        
        errorViewForm.show();
        
        // also print the error on console
        logger.log(Level.SEVERE, "Terminal error:", t);

	}

	@Override
	public void userChangedButtonClick(User user) {
		// set konekti locale
		i18n.setCurrentLocale(user.getActiveLocale());
		
		// remove all module tabs from workbech an all panels
		konektiLayout.removeModules(LOCATION.TOP);
		konektiLayout.removeModules(LOCATION.CENTER);
		konektiLayout.removeModules(LOCATION.LEFT);
		konektiLayout.removeModules(LOCATION.RIGHT);
		konektiLayout.removeModules(LOCATION.BOTTON);
		
		// remove all items
		konektiLayout.getMenuLayout().removeItems();
		
		// regenerate all items
		initMenuManager(user);	
		
	}

}