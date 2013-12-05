package com.thingtrack.konekti.view.module.supplier.internal;

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
import org.vaadin.dialogs.ConfirmDialog;

import com.thingtrack.konekti.domain.Supplier;
import com.thingtrack.konekti.knowledge.service.api.SupplierKnowledgeService;
import com.thingtrack.konekti.service.api.AddressService;
import com.thingtrack.konekti.service.api.SequenceService;
import com.thingtrack.konekti.service.api.SupplierGroupService;
import com.thingtrack.konekti.service.api.SupplierService;
import com.thingtrack.konekti.service.api.SupplierTypeService;
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
import com.thingtrack.konekti.view.web.form.SupplierViewForm;

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

@SuppressWarnings("serial")
public class SupplierView extends AbstractView implements
		ClickRefreshButtonListener,
		ClickAddButtonListener, ClickEditButtonListener,
		ClickRemoveButtonListener, ClickFilterButtonListener,
		ClickPrintButtonListener, ClickImportButtonListener {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private DataGridView dgSupplier;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private SequenceService sequenceService;	
	private AddressService addressService;
	private SupplierService supplierService;
	private SupplierTypeService supplierTypeService;
	private SupplierGroupService supplierGroupService;
	private SupplierKnowledgeService supplierKnowledgeService;
	
	private BindingSource<Supplier> bsSupplier = new BindingSource<Supplier>(
			Supplier.class, 0);

	private NavigationToolbar navigationToolbar;
	private EditionToolbar editionToolbar;
	private BoxToolbar boxToolbar;

	private IViewContainer viewContainer;
	private IWorkbenchContext context;
	
	private static final String IMPORT_SHEET_NAME = "SUPPLIER";
	
	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public SupplierView(IWorkbenchContext context, IViewContainer viewContainer) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.context = context;
		
		// set Slide View Services and ViewContainer to navigate
		this.viewContainer = viewContainer;
		
		this.sequenceService = SupplierViewContainer.getSequenceService();
		this.supplierService = SupplierViewContainer.getSupplierService();
		this.addressService = SupplierViewContainer.getAddressService();
		this.supplierTypeService = SupplierViewContainer.getSupplierTypeService();
	    this.supplierGroupService = SupplierViewContainer.getSupplierGroupService();
	    this.supplierKnowledgeService = SupplierViewContainer.getSupplierKnowledgeService();		
	    
		// initialize datasource views
		initView();
	}

	private void initView() {
		// initialize Slide View Organization View
		dgSupplier.setImmediate(true);

		refreshBindindSource();

		// STEP 01: create grid view for slide Organization View
		try {
			dgSupplier.setBindingSource(bsSupplier);
			dgSupplier.addGeneratedColumn(AddressStreetColumn.ADDRESS_STREET_COLUMN_ID, new AddressStreetColumn());
			
			dgSupplier.setVisibleColumns(new String[] { "agentId", "code", "name", "description", "vat", "comment", "supplierType.description", "supplierGroup.description", AddressStreetColumn.ADDRESS_STREET_COLUMN_ID, "active" });
			dgSupplier.setColumnHeaders(new String[] { "Id", "Código", "Nombre", "Descripción", "VAT", "Comentarios", "Tipo", "Grupo", "Dirección", "Activo" });		
			dgSupplier.setEditable(true);
			dgSupplier.setTableFieldFactory(new TableFieldFactory() {
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

			dgSupplier.setColumnCollapsed("agentId", true);
			dgSupplier.setColumnCollapsed(AddressStreetColumn.ADDRESS_STREET_COLUMN_ID, true);
		} catch (Exception ex) {
			ex.getMessage();
		}

		// STEP 02: create toolbar for slide Employee Agent View
		navigationToolbar = new NavigationToolbar(0, bsSupplier, viewContainer);
		editionToolbar = new EditionToolbar(1, bsSupplier);
		boxToolbar = new BoxToolbar(2, bsSupplier);
		
		navigationToolbar.addListenerRefreshButton(this);
		navigationToolbar.setUpButton(false);
		navigationToolbar.setDownButton(false);
		
		editionToolbar.addListenerAddButton(this);
		editionToolbar.addListenerEditButton(this);
		editionToolbar.addListenerDeleteButton(this);

		editionToolbar.setPermission(context.getUser(), viewContainer.getModule().getSymbolicName(), viewContainer.getModule().getVersion());
		
		boxToolbar.addListenerFilterButton(this);
		boxToolbar.addListenerPrintButton(this);
		boxToolbar.addListenerImportButton(this);
		
		boxToolbar.setImportButton(true);
		
		dgSupplier.addListenerAddButton(this);
		dgSupplier.addListenerEditButton(this);
		dgSupplier.addListenerDeleteButton(this);
		
		removeAllToolbar();

		addToolbar(navigationToolbar);
		addToolbar(editionToolbar);
		addToolbar(boxToolbar);

	}

	private void refreshBindindSource() {
		try {
			bsSupplier.removeAllItems();
			bsSupplier.addAll(supplierService.getAll(context.getUser()));

			bsSupplier.addNestedContainerProperty("supplierType.description");
			bsSupplier.addNestedContainerProperty("supplierGroup.description");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refreshButtonClick(NavigationToolbar.ClickNavigationEvent event) {
		refreshBindindSource();

	}

	private void refreshDataGridView(Supplier supplierSaved) {
		if(bsSupplier.containsId(supplierSaved)){			
			Supplier previousSupplier = bsSupplier.prevItemId(supplierSaved);
			
			bsSupplier.removeItem(supplierSaved);
			bsSupplier.addItemAfter(previousSupplier, supplierSaved);
			bsSupplier.setItemId(supplierSaved);
		}
		else
			bsSupplier.addItem(supplierSaved);
		
	}
	
	@Override
	public void addButtonClick(ClickNavigationEvent event) {
		Supplier supplier = null;
		try {
			supplier = supplierService.createNewSupplier(context.getUser().getActiveOrganization());
		} catch (Exception e) {
			throw new RuntimeException(
					"¡No se pudo crear el nuevo código proveedor!",
					e);
		}

		try {
			@SuppressWarnings("unused")
			WindowDialog<Supplier> windowDialog = new WindowDialog<Supplier>(
					getWindow(), "Nuevo Proveedor", "Guardar",
					DialogResult.SAVE, "Cancelar", DialogResult.CANCEL,
					new SupplierViewForm(context), supplier,
					new WindowDialog.CloseWindowDialogListener<Supplier>() {
						public void windowDialogClose(
								WindowDialog<Supplier>.CloseWindowDialogEvent<Supplier> event) {
							if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
								return;

							try {
					    		Supplier savingSupplier = event.getDomainEntity();
					    		
					    		Supplier savedsupplier = supplierService.save(savingSupplier);
					    		
					    		refreshDataGridView(savedsupplier);

							} catch (Exception e) {
								throw new RuntimeException("¡No se pudo crear el nuevo proveedor!", e);

							}
						}

					});
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("¡No se pudo abrir el formulario Nuevo Proveedor!", e);
		} catch (Exception e) {
			throw new RuntimeException("¡No se pudo abrir el formulario Nuevo Proveedor!", e);
		}

	}

	@Override
	public void editButtonClick(ClickNavigationEvent event) {
		Supplier editingSupplier = (Supplier) event.getRegister();

		try {
			@SuppressWarnings("unused")
			WindowDialog<Supplier> windowDialog = new WindowDialog<Supplier>(
					getWindow(), "Editor Proveedor", "Guardar",
					DialogResult.SAVE, "Cancelar", DialogResult.CANCEL,
					new SupplierViewForm(context), editingSupplier,
					new WindowDialog.CloseWindowDialogListener<Supplier>() {
						public void windowDialogClose(
								WindowDialog<Supplier>.CloseWindowDialogEvent<Supplier> event) {
							if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
								return;

							try {
					    		Supplier savingSupplier = event.getDomainEntity();
					    		
					    		Supplier savedSupplier = supplierService.save(savingSupplier);
					    		
					    		refreshDataGridView(savedSupplier);
										
							} catch (Exception e) {
								throw new RuntimeException("¡No se pudo modificar el proveedor!", e);

							}
						}

					});
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("¡No se pudo abrir el formulario Editor Proveedor!", e);
		} catch (Exception e) {
			throw new RuntimeException("¡No se pudo abrir el formulario Editor Proveedor!", e);
		}

	}

	@Override
	public void deleteButtonClick(ClickNavigationEvent event) {
		final Supplier editingSupplier = (Supplier) event.getRegister();

		if (editingSupplier == null)
			return;

		ConfirmDialog.show(getWindow(), "Borrar Proveedor", "¿Estás seguro?",
				"Si", "No", new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								supplierService.delete(editingSupplier);
		            			
		            			bsSupplier.removeItem(editingSupplier);		            			

							} catch (IllegalArgumentException e) {
								throw new RuntimeException("¡No se pudo borrar el proveedor!", e);
							} catch (Exception e) {
								throw new RuntimeException("¡No se pudo borrar el proveedor!", e);
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
		dgSupplier = new DataGridView();
		dgSupplier.setImmediate(false);
		dgSupplier.setWidth("100.0%");
		dgSupplier.setHeight("100.0%");
		mainLayout.addComponent(dgSupplier);
		mainLayout.setExpandRatio(dgSupplier, 1.0f);

		return mainLayout;
	}

	private class AddressStreetColumn implements ColumnGenerator {

		static final String ADDRESS_STREET_COLUMN_ID = "address_street_column-id";

		@Override
		public Object generateCell(CustomTable source, Object itemId,
				Object columnId) {

			Label addressStreetLabel = new Label();

			Supplier supplier = (Supplier) itemId;

			if (supplier.getAddress() != null) {
				String direction = supplier.getAddress().getStreet();

				if (supplier.getAddress().getLetter() != null)
					direction += ","
							+ supplier.getAddress().getLetter();

				if (supplier.getAddress().getNumber() != null)
					direction += ","
							+ supplier.getAddress().getNumber();

				if (supplier.getAddress().getCity() != null)
					direction += "," + supplier.getAddress().getCity();

				addressStreetLabel.setValue(direction);
			}

			return addressStreetLabel;
		}

	}

	@Override
	public void filterButtonClick(BoxToolbar.ClickNavigationEvent event) {
		dgSupplier.setFilterBarVisible();

	}

	@Override
	public void printButtonClick(BoxToolbar.ClickNavigationEvent event) {
		try {
			dgSupplier.print("Maestro Proveedores");
		}
		catch (Exception e) {
			throw new RuntimeException("¡No se pudo imprimir el informe!", e);
		}
		
	}

	@Override
	public void importButtonClick(BoxToolbar.ClickNavigationEvent event) {
		List<Supplier> suppliers = new ArrayList<Supplier>();
		
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
						
						Excel2Entity excel2Entity = new Excel2Entity(context, sequenceService, addressService, supplierGroupService, supplierTypeService, row);
						
						suppliers.add(excel2Entity.parse());
						
					}
					
					try {
						supplierKnowledgeService.importSuppliers(suppliers);
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
		// TODO Auto-generated method stub
		
	}

}
