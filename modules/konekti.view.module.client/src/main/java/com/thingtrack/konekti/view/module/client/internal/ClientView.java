package com.thingtrack.konekti.view.module.client.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

import com.thingtrack.konekti.domain.Client;
import com.thingtrack.konekti.knowledge.service.api.ClientKnowledgeService;
import com.thingtrack.konekti.service.api.AddressService;
import com.thingtrack.konekti.service.api.ClientGroupService;
import com.thingtrack.konekti.service.api.ClientService;
import com.thingtrack.konekti.service.api.ClientTypeService;
import com.thingtrack.konekti.service.api.SequenceService;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.ui.AbstractView;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar.ClickFilterButtonListener;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar.ClickImportButtonListener;
import com.thingtrack.konekti.view.addon.ui.BoxToolbar.ClickPrintButtonListener;
import com.thingtrack.konekti.view.addon.ui.DataGridView;
import com.thingtrack.konekti.view.addon.ui.EditionToolbar;
import com.thingtrack.konekti.view.addon.ui.EditionToolbar.ClickAddButtonListener;
import com.thingtrack.konekti.view.addon.ui.EditionToolbar.ClickEditButtonListener;
import com.thingtrack.konekti.view.addon.ui.EditionToolbar.ClickNavigationEvent;
import com.thingtrack.konekti.view.addon.ui.EditionToolbar.ClickRemoveButtonListener;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar;
import com.thingtrack.konekti.view.addon.ui.NavigationToolbar.ClickRefreshButtonListener;
import com.thingtrack.konekti.view.addon.ui.WindowDialog;
import com.thingtrack.konekti.view.addon.ui.WindowDialog.DialogResult;
import com.thingtrack.konekti.view.kernel.IWorkbenchContext;
import com.thingtrack.konekti.view.kernel.ui.layout.IViewContainer;
import com.thingtrack.konekti.view.web.form.ClientViewForm;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Container;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.CustomTable.ColumnGenerator;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.dialogs.ConfirmDialog;

@SuppressWarnings("serial")
public class ClientView extends AbstractView implements
		ClickRefreshButtonListener,
		ClickAddButtonListener, ClickEditButtonListener,
		ClickRemoveButtonListener, ClickFilterButtonListener,
		ClickPrintButtonListener, ClickImportButtonListener {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private DataGridView dgClient;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */	
	
	private SequenceService sequenceService;
	private ClientService clientService;
	private AddressService addressService;
	private ClientTypeService clientTypeService;
	private ClientGroupService clientGroupService;
	private ClientKnowledgeService clientKnowledgeService;
	
	private BindingSource<Client> bsClient = new BindingSource<Client>(Client.class, 0);

	private NavigationToolbar navigationToolbar;
	private EditionToolbar editionToolbar;
	private BoxToolbar boxToolbar;

	private IViewContainer viewContainer;
	private IWorkbenchContext context;
	
	private static final String IMPORT_SHEET_NAME = "CLIENT";
	
	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public ClientView(IWorkbenchContext context, IViewContainer viewContainer) {
		this.context = context;
		
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		// set Slide View Services and ViewContainer to navigate		
		this.viewContainer = viewContainer;
		this.sequenceService = ClientViewContainer.getSequenceService();
		this.clientService = ClientViewContainer.getClientService();
		this.addressService = ClientViewContainer.getAddressService();
		this.clientTypeService = ClientViewContainer.getClientTypeService();
	    this.clientGroupService = ClientViewContainer.getClientGroupService();
	    this.clientKnowledgeService = ClientViewContainer.getClientKnowledgeService();
	    
		// initialize datasource views
		initView();
	}

	private void initView() {
		// initialize Slide View Organization View
		dgClient.setImmediate(true);
		dgClient.setSelectable(true);
		
		refreshBindindSource();

		// STEP 01: create grid view for slide Organization View
		try {
			dgClient.setBindingSource(bsClient);
			dgClient.addGeneratedColumn(AddressStreetColumn.ADDRESS_STREET_COLUMN_ID, new AddressStreetColumn());
			dgClient.setVisibleColumns(new String[] { "agentId", "code", "name", "description", "vat", "comment", "clientType.description", "clientGroup.description", AddressStreetColumn.ADDRESS_STREET_COLUMN_ID, "active" });
			dgClient.setColumnHeaders(new String[] { "Id", "Código", "Nombre", "Descripción", "VAT", "Comentarios", "Tipo", "Grupo", "Dirección", "Activo" });				
			dgClient.setEditable(true);
			dgClient.setTableFieldFactory(new TableFieldFactory() {
				@Override
				public Field createField(Container container, Object itemId,
						Object propertyId, Component uiContext) {
					if ("active".equals(propertyId)) {
						CheckBox field = new CheckBox();
						field.setReadOnly(true);
						return field;
					}

					return null;
				}
			});

			dgClient.setColumnCollapsed("agentId", true);
			dgClient.setColumnCollapsed(AddressStreetColumn.ADDRESS_STREET_COLUMN_ID, true);
		} catch (Exception ex) {
			ex.getMessage();
		}

		// STEP 02: create toolbar for slide Employee Agent View
		navigationToolbar = new NavigationToolbar(0, bsClient, viewContainer);
		editionToolbar = new EditionToolbar(1, bsClient);
		boxToolbar = new BoxToolbar(2, bsClient);

		navigationToolbar.addListenerRefreshButton(this);
		navigationToolbar.setUpButton(false);
		navigationToolbar.setDownButton(false);
		
		editionToolbar.addListenerAddButton(this);
		editionToolbar.addListenerEditButton(this);
		editionToolbar.addListenerDeleteButton(this);
		
		editionToolbar.setPermission(context.getUser(), viewContainer.getModule().getSymbolicName(), viewContainer.getModule().getVersion());
		
		boxToolbar.setImportButton(true);
		
		boxToolbar.addListenerFilterButton(this);
		boxToolbar.addListenerPrintButton(this);	
		boxToolbar.addListenerImportButton(this);		
		
		dgClient.addListenerAddButton(this);
		dgClient.addListenerEditButton(this);
		dgClient.addListenerDeleteButton(this);
		
		removeAllToolbar();

		addToolbar(navigationToolbar);
		addToolbar(editionToolbar);
		addToolbar(boxToolbar);

	}

	private void refreshBindindSource() {
		try {
			bsClient.removeAllItems();
			bsClient.addAll(clientService.getAll(context.getUser()));

			bsClient.addNestedContainerProperty("clientType.description");
			bsClient.addNestedContainerProperty("clientGroup.description");
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("¡No se pudo refrescar los Clientes!", e);
		} catch (Exception e) {
			throw new RuntimeException("¡No se pudo refrescar los Clientes!", e);
		}
	}

	@Override
	public void refreshButtonClick(NavigationToolbar.ClickNavigationEvent event) {
		refreshBindindSource();

	}

	private void refreshDataGridView(Client clientSaved) {
		if (bsClient.containsId(clientSaved)) {
			Client previousClient = bsClient.prevItemId(clientSaved);

			bsClient.removeItem(clientSaved);
			bsClient.addItemAfter(previousClient, clientSaved);
			bsClient.setItemId(clientSaved);
		} else
			bsClient.addItem(clientSaved);

	}

	@Override
	public void addButtonClick(ClickNavigationEvent event) {
		Client client = null;
		try {
			client = clientService.createNewClient(context.getUser().getActiveOrganization());
		} catch (Exception e) {
			throw new RuntimeException(
					"¡No se pudo crear el nuevo código cliente!",
					e);
		}	
		
		try {
			@SuppressWarnings("unused")
			WindowDialog<Client> windowDialog = new WindowDialog<Client>(
					getWindow(), getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.add.tittle"), getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.button.left"), DialogResult.SAVE,
							getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.button.right"), DialogResult.CANCEL, new ClientViewForm(context), client,
					new WindowDialog.CloseWindowDialogListener<Client>() {
						public void windowDialogClose(
								WindowDialog<Client>.CloseWindowDialogEvent<Client> event) {
							if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
								return;

							try {
								Client savingClient = event.getDomainEntity();

								Client savedClient = clientService
										.save(savingClient);

								refreshDataGridView(savedClient);
							} catch (Exception e) {
								throw new RuntimeException(
										"¡No se pudo crear el nuevo cliente!",
										e);

							}
						}

					});
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Nuevo Cliente!", e);
		} catch (Exception e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Nuevo Cliente!", e);
		}

	}

	@Override
	public void editButtonClick(ClickNavigationEvent event) {
		Client editingClient = (Client) event.getRegister();

		try {
			@SuppressWarnings("unused")
			WindowDialog<Client> windowDialog = new WindowDialog<Client>(
					getWindow(), getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.edit.tittle"), getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.button.left"),
					DialogResult.SAVE, getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.button.right"), DialogResult.CANCEL,
					new ClientViewForm(context), editingClient,
					new WindowDialog.CloseWindowDialogListener<Client>() {
						public void windowDialogClose(
								WindowDialog<Client>.CloseWindowDialogEvent<Client> event) {
							if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
								return;

							try {
								Client savingClient = event.getDomainEntity();

								Client savedClient = clientService.save(savingClient);

								refreshDataGridView(savedClient);

							} catch (Exception e) {
								throw new RuntimeException(
										"¡No se pudo modificar el cliente!", e);

							}
						}

					});
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Editor Cliente!", e);
		} catch (Exception e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Editor Cliente!", e);
		}

	}

	@Override
	public void deleteButtonClick(ClickNavigationEvent event) {
		final Client editingClient = (Client) event.getRegister();

		if (editingClient == null)
			return;

		ConfirmDialog.show(getWindow(), getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.remove.tittle"), getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.remove.confirmation"),
				getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.remove.confirmation.yes"), getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.windowDialog.remove.confirmation.no"), new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								clientService.delete(editingClient);

								bsClient.removeItem(editingClient);

							} catch (IllegalArgumentException e) {
								throw new RuntimeException(
										"¡No se pudo borrar el cliente!", e);
							} catch (Exception e) {
								throw new RuntimeException(
										"¡No se pudo borrar el cliente!", e);
							}
						}
					}
				});

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

		// dgEmployee
		dgClient = new DataGridView();
		dgClient.setImmediate(false);
		dgClient.setWidth("100.0%");
		dgClient.setHeight("100.0%");
		mainLayout.addComponent(dgClient);
		mainLayout.setExpandRatio(dgClient, 1.0f);

		return mainLayout;
	}

	private class AddressStreetColumn implements ColumnGenerator {

		static final String ADDRESS_STREET_COLUMN_ID = "address_street_column-id";

		@Override
		public Object generateCell(CustomTable source, Object itemId,
				Object columnId) {

			Label addressStreetLabel = new Label();

			Client client = (Client) itemId;

			if (client.getAddress() != null) {
				String direction = client.getAddress().getStreet();

				if (client.getAddress().getLetter() != null)
					direction += "," + client.getAddress().getLetter();

				if (client.getAddress().getNumber() != null)
					direction += "," + client.getAddress().getNumber();

				if (client.getAddress().getCity() != null)
					direction += "," + client.getAddress().getCity();

				addressStreetLabel.setValue(direction);
			}

			return addressStreetLabel;
		}

	}

	@Override
	public void filterButtonClick(BoxToolbar.ClickNavigationEvent event) {
		dgClient.setFilterBarVisible();
		
	}

	@Override
	public void printButtonClick(BoxToolbar.ClickNavigationEvent event) {
		try {
			dgClient.print("Maestro Clientes");
		}
		catch (Exception e) {
			throw new RuntimeException("¡No se pudo imprimir el informe!", e);
		}
		
	}

	@Override
	public void importButtonClick(BoxToolbar.ClickNavigationEvent event) {
		List<Client> clients = new ArrayList<Client>();
		
		if (event.getFile() == null)
			return;
		
		InputStream file = new ByteArrayInputStream(event.getFile());
		
		// Load XLS file
		POIFSFileSystem fs;
		try {
			fs = new POIFSFileSystem(file);
			HSSFWorkbook workbook = new HSSFWorkbook(fs);
			workbook.setMissingCellPolicy(Row.RETURN_NULL_AND_BLANK);
			
			for (int x = 0; x < workbook.getNumberOfSheets(); x++) {
				HSSFSheet sheet = workbook.getSheetAt(x);
				
				if (IMPORT_SHEET_NAME.equals(sheet.getSheetName())) {
					for(int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++){
						HSSFRow row = sheet.getRow(rowIndex);
						
						if(row == null)
							break;
						
						Excel2Entity excel2Entity = new Excel2Entity(context, sequenceService, addressService, clientGroupService, clientTypeService, row);
						
						clients.add(excel2Entity.parse());
						
					}
					
					try {
						clientKnowledgeService.importClients(clients);
					} catch (Exception e) {
						throw new RuntimeException("¡No se pudo importar el fichero!", e);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("¡No se pudo importar el fichero!", e);
		}
	}

	@Override
	protected void updateLabels() {		
		dgClient.setColumnHeaders(new String[] { getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.clientId"), 
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.code"), 
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.name"), 
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.description"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.vat"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.comment"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.clientType"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.clientGroup"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.address"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.client.internal.ClientView.dgClient.column.active")});
		
	}
	
}
