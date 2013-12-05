package com.thingtrack.konekti.view.module.sensor.internal;

import com.thingtrack.konekti.service.sensor.api.CaptureLocationService;
import com.thingtrack.konekti.service.sensor.api.CaptureMessageService;
import com.thingtrack.konekti.service.sensor.api.CaptureTelemetryService;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.ui.AbstractView;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar.ClickFilterButtonListener;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar.ClickPrintButtonListener;
import com.thingtrack.konekti.view.addon.ui.DataGridView;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar.ClickDownButtonListener;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar.ClickNavigationEvent;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar.ClickRefreshButtonListener;
import com.thingtrack.konekti.view.kernel.IWorkbenchContext;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewContainer;
import com.thingtrack.konekti.domain.sensor.CaptureMessage;
import com.thingtrack.konekti.domain.sensor.CaptureLocation;
import com.thingtrack.konekti.domain.sensor.CaptureTelemetry;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;

@SuppressWarnings("serial")
public class CaptureView extends AbstractView  implements ClickDownButtonListener, 
	ClickRefreshButtonListener, ClickFilterButtonListener, ClickPrintButtonListener {

	@AutoGenerated
	private VerticalLayout mainLayout;

	@AutoGenerated
	private TabSheet tabSheetMessage;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	private DataGridView dgCaptureMessage;
	private DataGridView dgCaptureLocation;
	private DataGridView dgCaptureTelemetry;
	
	private CaptureMessageService captureMessageService;
	private CaptureLocationService captureLocationService;
	private CaptureTelemetryService captureTelemetryService;
		
	private BindingSource<CaptureMessage> bsCaptureMessage = new BindingSource<CaptureMessage>(CaptureMessage.class, 0);
	private BindingSource<CaptureLocation> bsCaptureLocation = new BindingSource<CaptureLocation>(CaptureLocation.class, 0);
	private BindingSource<CaptureTelemetry> bsCaptureTelemetry = new BindingSource<CaptureTelemetry>(CaptureTelemetry.class, 0);
	
	private NavigationToolbar navigationToolbar;
	private BoxToolbar boxToolbar;
		
	private IViewContainer viewContainer;
	private IWorkbenchContext context;
	
	private final static String CAPTURE_MESSAGE_TAB_NAME = "Captura Mensajes";
	private final static String CAPTURE_LOCATION_TAB_NAME = "Captura Geolocalizaciones";
	private final static String CAPTURE_TELEMETRY_TAB_NAME = "Captura Telemétricas";
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public CaptureView( IWorkbenchContext context, IViewContainer viewContainer) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.viewContainer = viewContainer;
		this.context = context;
		
		this.captureMessageService = SensorViewContainer.getCaptureMessageService();
		this.captureLocationService = SensorViewContainer.getCaptureLocationService();
		this.captureTelemetryService = SensorViewContainer.getCaptureTelemetryService();
		
		dgCaptureMessage = new DataGridView();
		dgCaptureMessage.setSizeFull();
		dgCaptureMessage.setImmediate(true);
		tabSheetMessage.addTab(dgCaptureMessage, CAPTURE_MESSAGE_TAB_NAME);
		
		dgCaptureLocation = new DataGridView();
		dgCaptureLocation.setSizeFull();
		dgCaptureLocation.setImmediate(true);
		tabSheetMessage.addTab(dgCaptureLocation, CAPTURE_LOCATION_TAB_NAME);
			
		dgCaptureTelemetry = new DataGridView();
		dgCaptureTelemetry.setSizeFull();
		dgCaptureTelemetry.setImmediate(true);
		tabSheetMessage.addTab(dgCaptureTelemetry, CAPTURE_TELEMETRY_TAB_NAME);
		
		tabSheetMessage.setSelectedTab(dgCaptureMessage);
		
		tabSheetMessage.addListener(new SelectedTabChangeListener() {
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				RefreshDataSource(event.getTabSheet().getTab(tabSheetMessage.getSelectedTab()).getCaption());
								
			}
		});
	
		RefreshDataSource(tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption());
	}
	
	private void RefreshDataSource(String tabName) {
		if (tabName.equals(CAPTURE_MESSAGE_TAB_NAME)) {
			refreshCaptureMessageBindindSource();
			injectCaptureMessageBindingSource();
		}
		else if (tabName.equals(CAPTURE_LOCATION_TAB_NAME)) {
			refreshCaptureLocationBindindSource();
			injectCaptureLocationBindingSource();
		}
		else if (tabName.equals(CAPTURE_TELEMETRY_TAB_NAME)) {
			refreshCaptureTelemetryBindindSource();
			injectTelemetryBindingSource();
		}	
	}
			
	private void refreshCaptureMessageBindindSource() {
		try {			
			bsCaptureMessage.removeAllItems();
			bsCaptureMessage.addAll(captureMessageService.getAll());					
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void injectCaptureMessageBindingSource() {
		try {					
			dgCaptureMessage.setBindingSource(bsCaptureMessage);
			dgCaptureMessage.setVisibleColumns(new String[] { "identifier", "message", "time"} );       
			dgCaptureMessage.setColumnHeaders(new String[] { "Identificador", "Mensaje", "Fecha" } );
			
			// create toolbar for slide
			navigationToolbar = new NavigationToolbar(0, bsCaptureMessage, viewContainer);
			boxToolbar = new BoxToolbar(1, bsCaptureMessage);
			
			navigationToolbar.addListenerRefreshButton(this);
			navigationToolbar.addListenerDownButton(this);		
						
			boxToolbar.addListenerFilterButton(this);
			boxToolbar.addListenerPrintButton(this);
						
			removeAllToolbar();
			
			addToolbar(navigationToolbar);
			addToolbar(boxToolbar);
			
			context.getToolbarManager().addToolbars(toolbars);
			
		} catch (Exception ex) {
			ex.getMessage();
			
		}
		
	}
	
	private void refreshCaptureLocationBindindSource() {
		try {			
			bsCaptureLocation.removeAllItems();
			bsCaptureLocation.addAll(captureLocationService.getAll());						
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void injectCaptureLocationBindingSource() {
		try {				
			dgCaptureLocation.setBindingSource(bsCaptureLocation);
			dgCaptureLocation.setVisibleColumns(new String[] { "identifier", "accuracy", "bearing", "longitude", "altitude", "provider", "speed", "time"} );       
			dgCaptureLocation.setColumnHeaders(new String[] { "Identificador", "Precisión", "Azimuth", "Longitud", "Latotud", "Proveedor", "Velocidad", "Fecha" } );
			
			// create toolbar for slide
			navigationToolbar = new NavigationToolbar(0, bsCaptureLocation, viewContainer);
			boxToolbar = new BoxToolbar(1, bsCaptureLocation);
			
			navigationToolbar.addListenerRefreshButton(this);
			navigationToolbar.addListenerDownButton(this);	
			
			boxToolbar.addListenerFilterButton(this);
			boxToolbar.addListenerPrintButton(this);
			
			removeAllToolbar();
			
			addToolbar(navigationToolbar);
			addToolbar(boxToolbar);
			
			context.getToolbarManager().addToolbars(toolbars);
			
		} catch (Exception ex) {
			ex.getMessage();
			
		}
		
	}

	private void refreshCaptureTelemetryBindindSource() {
		try {			
			bsCaptureTelemetry.removeAllItems();
			bsCaptureTelemetry.addAll(captureTelemetryService.getAll());						
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void injectTelemetryBindingSource() {
		try {					
			dgCaptureTelemetry.setBindingSource(bsCaptureTelemetry);
			dgCaptureTelemetry.setVisibleColumns(new String[] { "identifier",
															    "identifieId",
															    "time1",
															    "engSpeed",
															    "accel",
															    "TCO_Speed",
															    "TCO_MD",
															    "TCO_OS",
															    "TCO_DI",
															    "TCO_DP",
															    "TCO_HI",
															    "TCO_EV",
															    "TCO_D1_PR",
															    "TCO_D1_WS",
															    "TCO_D1_TS",
															    "TCO_D2_PR",
															    "TCO_D2_WS",
															    "TCO_D2_TS",
															    "vehSpeed",
															    "CC",
															    "BR",
															    "CS",
															    "PB",
															    "distance",
															    "engHours",
															    "fuelC",
															    "engTemp",
															    "fuelLev",
															    "vehID",
															    "FMS_Versd",
															    "FMS_Diag",
															    "FMS_Requ",
															    "gear_S",
															    "gear_C",
															    "DC1_P",
															    "DC1_R",
															    "DC1_S",
															    "DC2_1",
															    "DC2_2",
															    "DC2_3",
															    "DC2_4",
															    "DC2_5",
															    "DC2_6",
															    "DC2_7",
															    "DC2_8",
															    "DC2_9",
															    "DC2_10",
															    "bellowPr_FAL",
															    "bellowPr_FAR",
															    "bellowPr_RAL",
															    "bellowPr_RAR",
															    "brakePR_1",
															    "brakePr_2",
															    "altern_1",
															    "altern_2",
															    "altern_3",
															    "altern_4",
															    "dateTime",
															    "ambTemp",
															    "card1_ID",
															    "card1_Type",
															    "card1_Country",
															    "card2_ID",
															    "card2_Type",
															    "card2_Country",
															    "fuelEco_l_h",
															    "fuelEco_km_l",
															    "TS_1",
															    "TS_2",
															    "TS_3",
															    "TS_4",
															    "TS_5",
															    "TS_6",
															    "TS_7",
															    "TS_8",
															    "TS_9",
															    "TS_10",
															    "TS_11",
															    "TS_12",
															    "TS_13",
															    "TS_14",
															    "TS_15",
															    "TS_16",
															    "TS_17",
															    "TS_18",
															    "TS_19",
															    "TS_20",
															    "TS_21",
															    "TS_22",
															    "TS_23",
															    "TS_24",
															    "TS_25",
															    "TS_26",
															    "TS_27",
															    "TS_28",
															    "TS_29",
															    "TS_30",
															    "TS_31",
															    "TS_32",
															    "TS_33",
															    "TS_34",
															    "TS_35",
															    "TS_36",
															    "TS_37",
															    "TS_38",
															    "TS_39",
															    "TS_40",
															    "TS_41",
					                                            "time"} );       
			dgCaptureTelemetry.setColumnHeaders(new String[] { "Identificador",
																"identifier_Id",
															    "Time1",
															    "EngSpeed",
															    "Accel",
															    "TCO_Speed",
															    "TCO_MD",
															    "TCO_OS",
															    "TCO_DI",
															    "TCO_DP",
															    "TCO_HI",
															    "TCO_EV",
															    "TCO_D1_PR",
															    "TCO_D1_WS",
															    "TCO_D1_TS",
															    "TCO_D2_PR",
															    "TCO_D2_WS",
															    "TCO_D2_TS",
															    "VehSpeed",
															    "CC",
															    "BR",
															    "CS",
															    "PB",
															    "Distance",
															    "EngHours",
															    "FuelC",
															    "EngTemp",
															    "FuelLev",
															    "VehID",
															    "FMS_Versd",
															    "FMS_Diag",
															    "FMS_Requ",
															    "Gear_S",
															    "Gear_C",
															    "DC1_P",
															    "DC1_R",
															    "DC1_S",
															    "DC2_1",
															    "DC2_2",
															    "DC2_3",
															    "DC2_4",
															    "DC2_5",
															    "DC2_6",
															    "DC2_7",
															    "DC2_8",
															    "DC2_9",
															    "DC2_10",
															    "BellowPr_FAL",
															    "BellowPr_FAR",
															    "BellowPr_RAL",
															    "BellowPr_RAR",
															    "BrakePR_1",
															    "BrakePr_2",
															    "Altern_1",
															    "Altern_2",
															    "Altern_3",
															    "Altern_4",
															    "DateTime",
															    "AmbTemp",
															    "Card1_ID",
															    "Card1_Type",
															    "Card1_Country",
															    "Card2_ID",															    
															    "Card2_Type",
															    "Card2_Country",
															    "FuelEco_l_h",
															    "FuelEco_km_l",
															    "TS_1",
															    "TS_2",
															    "TS_3",
															    "TS_4",
															    "TS_5",
															    "TS_6",
															    "TS_7",
															    "TS_8",
															    "TS_9",
															    "TS_10",
															    "TS_11",
															    "TS_12",
															    "TS_13",
															    "TS_14",
															    "TS_15",
															    "TS_16",
															    "TS_17",
															    "TS_18",
															    "TS_19",
															    "TS_20",
															    "TS_21",
															    "TS_22",
															    "TS_23",
															    "TS_24",
															    "TS_25",
															    "TS_26",
															    "TS_27",
															    "TS_28",
															    "TS_29",
															    "TS_30",
															    "TS_31",
															    "TS_32",
															    "TS_33",
															    "TS_34",
															    "TS_35",
															    "TS_36",
															    "TS_37",
															    "TS_38",
															    "TS_39",
															    "TS_40",
															    "TS_41",
											                    "time" } );
			
			// create toolbar for slide
			navigationToolbar = new NavigationToolbar(0, bsCaptureTelemetry, viewContainer);
			boxToolbar = new BoxToolbar(1, bsCaptureTelemetry);
			
			navigationToolbar.addListenerRefreshButton(this);
			navigationToolbar.addListenerDownButton(this);		
			
			boxToolbar.addListenerFilterButton(this);
			boxToolbar.addListenerPrintButton(this);
			
			removeAllToolbar();
			
			addToolbar(navigationToolbar);
			addToolbar(boxToolbar);
			
			context.getToolbarManager().addToolbars(toolbars);
			
		} catch (Exception ex) {
			ex.getMessage();
			
		}
		
	}
	
	@Override
	public void downButtonClick(ClickNavigationEvent event) {				
		// roll to the detail Workshop Detail View
		viewContainer.getSliderView().rollNext();
						
	}
	
	@Override
	public void refreshButtonClick(ClickNavigationEvent event) {
		RefreshDataSource(tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption());
		
	}
		
	@Override
	public void filterButtonClick(BoxToolbar.ClickNavigationEvent event) {
		try {
			if (tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption().equals(CAPTURE_MESSAGE_TAB_NAME))
				dgCaptureMessage.setFilterBarVisible();
			else if (tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption().equals(CAPTURE_LOCATION_TAB_NAME))
				dgCaptureLocation.setFilterBarVisible();
			else if (tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption().equals(CAPTURE_TELEMETRY_TAB_NAME))
				dgCaptureTelemetry.setFilterBarVisible();				
		}
		catch (Exception e) {
			throw new RuntimeException("¡No se pudo filtrar la lista!", e);
		}		
		
	}

	@Override
	public void printButtonClick(BoxToolbar.ClickNavigationEvent event) {
		try {
			if (tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption().equals(CAPTURE_MESSAGE_TAB_NAME))
				dgCaptureMessage.print("Listado Captura Mensajes");
			else if (tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption().equals(CAPTURE_LOCATION_TAB_NAME))
				dgCaptureLocation.print("Listado Captura Geolocalizaciones");				
			else if (tabSheetMessage.getTab(tabSheetMessage.getSelectedTab()).getCaption().equals(CAPTURE_TELEMETRY_TAB_NAME))
				dgCaptureTelemetry.print("Listado Captura Telemétricas");
		}
		catch (Exception e) {
			throw new RuntimeException("¡No se pudo imprimir el informe!", e);
		}
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// tabSheetMessage
		tabSheetMessage = new TabSheet();
		tabSheetMessage.setImmediate(false);
		tabSheetMessage.setWidth("100.0%");
		tabSheetMessage.setHeight("100.0%");
		mainLayout.addComponent(tabSheetMessage);
		mainLayout.setExpandRatio(tabSheetMessage, 1.0f);
		
		return mainLayout;
	}

	@Override
	protected void updateLabels() {
		// TODO Auto-generated method stub
		
	}

}
