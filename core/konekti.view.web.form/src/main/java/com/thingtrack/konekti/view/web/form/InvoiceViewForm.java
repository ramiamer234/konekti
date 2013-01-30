package com.thingtrack.konekti.view.web.form;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.thingtrack.konekti.domain.Client;
import com.thingtrack.konekti.domain.InvoiceStatus;
import com.thingtrack.konekti.domain.InvoiceType;
import com.thingtrack.konekti.domain.Offer;
import com.thingtrack.konekti.domain.Organization;
import com.thingtrack.konekti.service.api.ClientService;
import com.thingtrack.konekti.service.api.InvoiceStatusService;
import com.thingtrack.konekti.service.api.InvoiceTypeService;
import com.thingtrack.konekti.service.api.OfferService;
import com.thingtrack.konekti.service.api.OrganizationService;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.web.form.field.FeedBackCollectionField;
import com.thingtrack.konekti.view.web.form.field.InvoiceLineCollectionField;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class InvoiceViewForm extends CustomComponent {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private TabSheet tbsInvoice;
	@AutoGenerated
	private TextField priceRealField;
	@AutoGenerated
	private TextField priceGapField;
	@AutoGenerated
	private TextField priceField;
	@AutoGenerated
	private ComboBox organizationField;
	@AutoGenerated
	private ComboBox offersField;
	@AutoGenerated
	private TextField observationField;
	@AutoGenerated
	private ComboBox invoiceTypeField;
	@AutoGenerated
	private ComboBox invoiceStatusField;
	@AutoGenerated
	private DateField invoiceDateField;
	@AutoGenerated
	private ComboBox invoiceClientField;
	@AutoGenerated
	private TextField discountField;
	@AutoGenerated
	private TextField codeField;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	private InvoiceLineCollectionField invoiceLinesField;
	private FeedBackCollectionField feedbacksField;
		
	// form services
	private OrganizationService organizationService;
	private InvoiceStatusService invoiceStatusService;
	private InvoiceTypeService invoiceTypeService;
	private OfferService offerService;
	private ClientService clientService;
	
	// organization type datasource
	private BeanItemContainer<Organization> bcOrganization = new BindingSource<Organization>(Organization.class);
	private BeanItemContainer<InvoiceStatus> bcInvoiceStatus = new BindingSource<InvoiceStatus>(InvoiceStatus.class);
	private BeanItemContainer<InvoiceType> bcInvoiceType = new BindingSource<InvoiceType>(InvoiceType.class);
	private BeanItemContainer<Offer> bcOffer = new BindingSource<Offer>(Offer.class);
	private BeanItemContainer<Client> bcClient = new BindingSource<Client>(Client.class);
	
	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 * @throws Exception 
	 * @throws IllegalArgumentException 
	 */
	public InvoiceViewForm() throws IllegalArgumentException, Exception {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		initComponents();
		
		// configure Organization Type data
		organizationField.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
		organizationField.setItemCaptionPropertyId("name");
		
		offersField.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
		offersField.setItemCaptionPropertyId("code");

		invoiceTypeField.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
		invoiceTypeField.setItemCaptionPropertyId("description");

		invoiceStatusField.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
		invoiceStatusField.setItemCaptionPropertyId("description");

		invoiceClientField.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
		invoiceClientField.setItemCaptionPropertyId("description");
		
		// configure Table Child Organization data		
		tbsInvoice.setCaption("Líneas Factura/Comentarios");
		
		invoiceLinesField = new InvoiceLineCollectionField();		
		invoiceLinesField.setSizeFull();
		tbsInvoice.addTab(invoiceLinesField, "Líneas Factura");
		
		feedbacksField = new FeedBackCollectionField();
		feedbacksField.setSizeFull();
		tbsInvoice.addTab(feedbacksField, "Comentarios");
				
		// get form services from OSGi Service Registry
		getServices();
		
		// load data sources
		loadData();
	}
		
	private void initComponents() {
		discountField.setNullRepresentation("");
		codeField.setNullRepresentation("");
		observationField.setNullRepresentation("");
		priceField.setNullRepresentation("");
		priceRealField.setNullRepresentation("");
		priceGapField.setNullRepresentation("");
		
		codeField.setRequiredError(codeField.getCaption() + " es un campo requerido");
		
		invoiceClientField.setNullSelectionAllowed(false);
		invoiceStatusField.setNullSelectionAllowed(false);
		invoiceTypeField.setNullSelectionAllowed(false);
		organizationField.setNullSelectionAllowed(false);
		
		codeField.focus();
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getServices() {
		try {
			BundleContext bundleContext = FrameworkUtil.getBundle(InvoiceViewForm.class).getBundleContext();

			ServiceReference organizationServiceReference = bundleContext.getServiceReference(OrganizationService.class.getName());
			organizationService = OrganizationService.class.cast(bundleContext.getService(organizationServiceReference));
			
			ServiceReference InvoiceStatusServiceReference = bundleContext.getServiceReference(InvoiceStatusService.class.getName());
			invoiceStatusService = InvoiceStatusService.class.cast(bundleContext.getService(InvoiceStatusServiceReference));
			
			ServiceReference invoiceTypeServiceReference = bundleContext.getServiceReference(InvoiceTypeService.class.getName());
			invoiceTypeService = InvoiceTypeService.class.cast(bundleContext.getService(invoiceTypeServiceReference));
			
			ServiceReference offerServiceReference = bundleContext.getServiceReference(OfferService.class.getName());
			offerService = OfferService.class.cast(bundleContext.getService(offerServiceReference));
			
			ServiceReference clientServiceReference = bundleContext.getServiceReference(ClientService.class.getName());
			clientService = ClientService.class.cast(bundleContext.getService(clientServiceReference));
		}
		catch (Exception e) {
			e.getMessage();
			
		}
		
	}
	
	private void loadData() throws IllegalArgumentException, Exception {		
		bcOrganization.removeAllItems();
		bcOrganization.addAll(organizationService.getAll());
		
		organizationField.setContainerDataSource(bcOrganization);
		
		bcInvoiceStatus.removeAllItems();
		bcInvoiceStatus.addAll(invoiceStatusService.getAll());
		
		invoiceStatusField.setContainerDataSource(bcInvoiceStatus);

		bcInvoiceType.removeAllItems();
		bcInvoiceType.addAll(invoiceTypeService.getAll());
		
		invoiceTypeField.setContainerDataSource(bcInvoiceType);

		bcOffer.removeAllItems();
		bcOffer.addAll(offerService.getAll());
		
		offersField.setContainerDataSource(bcOffer);

		bcClient.removeAllItems();
		bcClient.addAll(clientService.getAll());
		
		invoiceClientField.setContainerDataSource(bcClient);
		
	}
	
	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("663px");
		mainLayout.setHeight("418px");
		mainLayout.setMargin(true);
		
		// top-level component properties
		setWidth("663px");
		setHeight("418px");
		
		// codeField
		codeField = new TextField();
		codeField.setCaption("Código");
		codeField.setImmediate(false);
		codeField.setWidth("107px");
		codeField.setHeight("-1px");
		codeField.setRequired(true);
		mainLayout.addComponent(codeField, "top:18.0px;left:21.0px;");
		
		// discountField
		discountField = new TextField();
		discountField.setCaption("Descuento");
		discountField.setImmediate(false);
		discountField.setWidth("120px");
		discountField.setHeight("-1px");
		mainLayout.addComponent(discountField, "top:100.0px;left:520.0px;");
		
		// invoiceClientField
		invoiceClientField = new ComboBox();
		invoiceClientField.setCaption("Cliente");
		invoiceClientField.setImmediate(false);
		invoiceClientField.setWidth("479px");
		invoiceClientField.setHeight("-1px");
		invoiceClientField.setRequired(true);
		mainLayout.addComponent(invoiceClientField, "top:100.0px;left:19.0px;");
		
		// invoiceDateField
		invoiceDateField = new DateField();
		invoiceDateField.setCaption("Fecha Factura");
		invoiceDateField.setImmediate(false);
		invoiceDateField.setWidth("180px");
		invoiceDateField.setHeight("-1px");
		invoiceDateField.setInvalidAllowed(false);
		invoiceDateField.setRequired(true);
		invoiceDateField.setResolution(4);
		mainLayout.addComponent(invoiceDateField, "top:18.0px;left:460.0px;");
		
		// invoiceStatusField
		invoiceStatusField = new ComboBox();
		invoiceStatusField.setCaption("Estado");
		invoiceStatusField.setImmediate(false);
		invoiceStatusField.setWidth("180px");
		invoiceStatusField.setHeight("-1px");
		invoiceStatusField.setRequired(true);
		mainLayout.addComponent(invoiceStatusField, "top:58.0px;left:460.0px;");
		
		// invoiceTypeField
		invoiceTypeField = new ComboBox();
		invoiceTypeField.setCaption("Tipo");
		invoiceTypeField.setImmediate(false);
		invoiceTypeField.setWidth("180px");
		invoiceTypeField.setHeight("-1px");
		invoiceTypeField.setRequired(true);
		mainLayout.addComponent(invoiceTypeField, "top:57.0px;left:266.0px;");
		
		// observationField
		observationField = new TextField();
		observationField.setCaption("Observaciones");
		observationField.setImmediate(false);
		observationField.setWidth("479px");
		observationField.setHeight("100px");
		mainLayout.addComponent(observationField, "top:140.0px;left:21.0px;");
		
		// offersField
		offersField = new ComboBox();
		offersField.setCaption("Ofertas");
		offersField.setImmediate(false);
		offersField.setWidth("-1px");
		offersField.setHeight("-1px");
		mainLayout.addComponent(offersField, "top:18.0px;left:141.0px;");
		
		// organizationField
		organizationField = new ComboBox();
		organizationField.setCaption("Organización");
		organizationField.setImmediate(false);
		organizationField.setWidth("240px");
		organizationField.setHeight("-1px");
		organizationField.setRequired(true);
		mainLayout.addComponent(organizationField, "top:57.0px;left:20.0px;");
		
		// priceField
		priceField = new TextField();
		priceField.setCaption("Precio");
		priceField.setImmediate(false);
		priceField.setWidth("120px");
		priceField.setHeight("-1px");
		mainLayout.addComponent(priceField, "top:140.0px;left:520.0px;");
		
		// priceGapField
		priceGapField = new TextField();
		priceGapField.setCaption("Diferencia Precio");
		priceGapField.setImmediate(false);
		priceGapField.setWidth("120px");
		priceGapField.setHeight("-1px");
		mainLayout.addComponent(priceGapField, "top:220.0px;left:520.0px;");
		
		// priceRealField
		priceRealField = new TextField();
		priceRealField.setCaption("Precio Real");
		priceRealField.setImmediate(false);
		priceRealField.setWidth("120px");
		priceRealField.setHeight("-1px");
		mainLayout.addComponent(priceRealField, "top:183.0px;left:520.0px;");
		
		// tbsInvoice
		tbsInvoice = new TabSheet();
		tbsInvoice.setImmediate(false);
		tbsInvoice.setWidth("621px");
		tbsInvoice.setHeight("140px");
		mainLayout.addComponent(tbsInvoice, "top:260.0px;left:19.0px;");
		
		return mainLayout;
	}
}