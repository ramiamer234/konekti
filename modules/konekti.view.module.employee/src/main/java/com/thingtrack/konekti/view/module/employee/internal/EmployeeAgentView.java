package com.thingtrack.konekti.view.module.employee.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.vaadin.dialogs.ConfirmDialog;

import com.thingtrack.konekti.domain.EmployeeAgent;
import com.thingtrack.konekti.knowledge.service.api.EmployeeAgentKnowledgeService;
import com.thingtrack.konekti.service.api.AddressService;
import com.thingtrack.konekti.service.api.EmployeeAgentService;
import com.thingtrack.konekti.service.api.EmployeeAgentStatusService;
import com.thingtrack.konekti.service.api.EmployeeAgentTypeService;
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
import com.thingtrack.konekti.view.web.form.EmployeeAgentViewForm;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class EmployeeAgentView extends AbstractView 
	implements ClickRefreshButtonListener, ClickAddButtonListener, ClickEditButtonListener, 
		ClickRemoveButtonListener, ClickFilterButtonListener, ClickPrintButtonListener, ClickImportButtonListener {
			
	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private DataGridView dgEmployeeAgent;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private EmployeeAgentService employeeAgentService;
	private AddressService addressService;
	private EmployeeAgentTypeService employeeAgentTypeService;
	private EmployeeAgentStatusService employeeAgentStatusService;
	private EmployeeAgentKnowledgeService employeeAgentKnowledgeService;
		
	private BindingSource<EmployeeAgent> bsEmployeeAgent =  new BindingSource<EmployeeAgent>(EmployeeAgent.class, 0);
	
	private NavigationToolbar navigationToolbar;
	private EditionToolbar editionToolbar;
	private BoxToolbar boxToolbar;
		
	private IViewContainer viewContainer;
	private IWorkbenchContext context;
	
	private static final String IMPORT_SHEET_NAME = "EMPLOYEE_AGENT";
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public EmployeeAgentView(IWorkbenchContext context, IViewContainer viewContainer) {		
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here	
		this.context = context;
		
		// set Slide View Services and ViewContainer to navigate
		this.viewContainer = viewContainer;
		this.addressService = EmployeeAgentViewContainer.getAddressService();
		this.employeeAgentService = EmployeeAgentViewContainer.getEmployeeAgentService();
		this.employeeAgentTypeService = EmployeeAgentViewContainer.getEmployeeAgentTypeService();
	    this.employeeAgentStatusService = EmployeeAgentViewContainer.getEmployeeAgentStatusService();
	    this.employeeAgentKnowledgeService = EmployeeAgentViewContainer.getEmployeeAgentKnowledgeService();		
		
		// initialize datasource views		
		initView();	
	}

	private void initView() {
		// initialize Slide View Organization View
		dgEmployeeAgent.setImmediate(true);
		
		refreshBindindSource();
		
		// STEP 01: create grid view for slide Organization View
		try {
			// add nested columns
			bsEmployeeAgent.addNestedContainerProperty("address.street");
			
			// add bind datasource
			dgEmployeeAgent.setBindingSource(bsEmployeeAgent);
			
			// add visible and collapsed  columns
			dgEmployeeAgent.setVisibleColumns(new String[] { "agentId", "workNumber", "tittle", "employeeAgentType.description", "shortname", "name", "surname", "email", "phone", "mobile", "workMobile", "fax", "nif", "facebookId", "comment", "seniority", "address.street", "employeeAgentStatus.description" } );       
			dgEmployeeAgent.setColumnHeaders(new String[] { "Id", "Número Trabajador", "Titular", "Tipo", "Nombre Corto", "Nombre", "Apellidos", "Email", "Teléfono", "Móvil", "Móbil Empresa", "Fax", "NIF", "Cuenta Facebook", "Comentarios", "Antigüedad", "Dirección", "Estado" } );
			
			dgEmployeeAgent.setColumnCollapsed("agentId", true);
			dgEmployeeAgent.setColumnCollapsed("tittle", true);
			dgEmployeeAgent.setColumnCollapsed("shortname", true);
			dgEmployeeAgent.setColumnCollapsed("fax", true);
			dgEmployeeAgent.setColumnCollapsed("nif", true);
			dgEmployeeAgent.setColumnCollapsed("facebookId", true);
			dgEmployeeAgent.setColumnCollapsed("comment", true);
			dgEmployeeAgent.setColumnCollapsed("phone", true);
			dgEmployeeAgent.setColumnCollapsed("mobile", true);
			dgEmployeeAgent.setColumnCollapsed("address.street", true);
			dgEmployeeAgent.setColumnCollapsed("seniority", true);
		}
		catch(Exception ex) {
			ex.getMessage();
		}
			
		// STEP 02: create toolbar for slide Employee Agent View
		navigationToolbar = new NavigationToolbar(0, bsEmployeeAgent, viewContainer);
		editionToolbar = new EditionToolbar(1, bsEmployeeAgent);
		boxToolbar = new BoxToolbar(2, bsEmployeeAgent);
		
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
		
		dgEmployeeAgent.addListenerAddButton(this);
		dgEmployeeAgent.addListenerEditButton(this);
		dgEmployeeAgent.addListenerDeleteButton(this);
		
		removeAllToolbar();
		
		addToolbar(navigationToolbar);
		addToolbar(editionToolbar);
		addToolbar(boxToolbar);

	}
	
	private void refreshBindindSource() {
		try {		
			bsEmployeeAgent.removeAllItems();
			bsEmployeeAgent.addAll(employeeAgentService.getAll(context.getUser()));
			
			bsEmployeeAgent.addNestedContainerProperty("employeeAgentType.description");
			bsEmployeeAgent.addNestedContainerProperty("employeeAgentStatus.description");
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

	private void refreshDataGridView(EmployeeAgent employeeAgentSaved) {
		if (bsEmployeeAgent.containsId(employeeAgentSaved)) {
			EmployeeAgent previousEmployeeAgent = bsEmployeeAgent.prevItemId(employeeAgentSaved);

			bsEmployeeAgent.removeItem(employeeAgentSaved);
			bsEmployeeAgent.addItemAfter(previousEmployeeAgent, employeeAgentSaved);
			bsEmployeeAgent.setItemId(employeeAgentSaved);
		} else
			bsEmployeeAgent.addItem(employeeAgentSaved);

	}
	
	@Override
	public void addButtonClick(ClickNavigationEvent event) {
		EmployeeAgent employeeAgent = null;
		try {
			employeeAgent = employeeAgentService.createNewEntity(context.getUser().getActiveOrganization());
		} catch (Exception e) {
			throw new RuntimeException(
					"¡No se pudo crear el nuevo empleado!",
					e);
		}

		try {
			@SuppressWarnings("unused")
			WindowDialog<EmployeeAgent> windowDialog = new WindowDialog<EmployeeAgent>(getWindow(), getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.add.tittle"), 
					getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.button.left"), DialogResult.SAVE, getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.button.right"), DialogResult.CANCEL, 
					new EmployeeAgentViewForm(context), employeeAgent, 
					new WindowDialog.CloseWindowDialogListener<EmployeeAgent>() {
			    public void windowDialogClose(WindowDialog<EmployeeAgent>.CloseWindowDialogEvent<EmployeeAgent> event) {
			    	if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
			    		return ;
			    	
			    	try {
			    		EmployeeAgent savingEmployeeAgent = event.getDomainEntity();
			    		
			    		EmployeeAgent savedEmployeeAgent = employeeAgentService.save(savingEmployeeAgent);
			    		
			    		refreshDataGridView(savedEmployeeAgent);
					} catch (Exception e) {
						throw new RuntimeException(
								"¡No se pudo crear el nuevo empleado!",
								e);
						
					}
			    }

			});
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Nuevo Empleado!", e);
		} catch (Exception e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Nuevo Empleado!", e);
		} 
		
	}
	
	@Override
	public void editButtonClick(ClickNavigationEvent event) {
		EmployeeAgent editingEmployeeAgent= (EmployeeAgent) event.getRegister();

		try {
			@SuppressWarnings("unused")
			WindowDialog<EmployeeAgent> windowDialog = new WindowDialog<EmployeeAgent>(getWindow(), getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.edit.tittle"), 
					getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.button.left"), DialogResult.SAVE, getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.button.right"), DialogResult.CANCEL, 
					new EmployeeAgentViewForm(context), editingEmployeeAgent, 
					new WindowDialog.CloseWindowDialogListener<EmployeeAgent>() {
			    public void windowDialogClose(WindowDialog<EmployeeAgent>.CloseWindowDialogEvent<EmployeeAgent> event) {
			    	if (event.getDialogResult() != WindowDialog.DialogResult.SAVE)
			    		return ;
			    	
			    	try {
			    		EmployeeAgent savingEmployeeAgent = event.getDomainEntity();
			    		
			    		EmployeeAgent savedEmployeeAgent = employeeAgentService.save(savingEmployeeAgent);
			    		
			    		refreshDataGridView(savedEmployeeAgent);
					} catch (Exception e) {
						throw new RuntimeException(
								"¡No se pudo modificar el empleado!", e);
						
					}
			    }

			});
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Editor Empleado!", e);
		} catch (Exception e) {
			throw new RuntimeException(
					"¡No se pudo abrir el formulario Editor Empleado!", e);
		} 
		
	}
	
	@Override
	public void deleteButtonClick(ClickNavigationEvent event) {
		final EmployeeAgent editingEmployeeAgent = (EmployeeAgent) event.getRegister();
		
		if (editingEmployeeAgent == null)
			return;
		
		ConfirmDialog.show(getWindow(), getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.remove.tittle"),
				getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.remove.confirmation"), getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.remove.confirmation.yes"), getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.windowDialog.remove.confirmation.no"),
		        new ConfirmDialog.Listener() {

		            public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		            		try {
		            			employeeAgentService.delete(editingEmployeeAgent);
		            					            			
		            			bsEmployeeAgent.removeItem(editingEmployeeAgent);
		            			
		            		} catch (IllegalArgumentException e) {
								throw new RuntimeException(
										"¡No se pudo borrar el empleado!", e);
		            		} catch (Exception e) {
								throw new RuntimeException(
										"¡No se pudo borrar el empleado!", e);
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
		dgEmployeeAgent = new DataGridView() {
		    @Override
		    protected String formatPropertyValue(Object rowId, Object colId, Property property) {
		    	// Format by property type
		        if (property.getType() == Date.class && property.getValue() != null) {
		            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		            
		            return df.format((Date)property.getValue());
		        }

		        return super.formatPropertyValue(rowId, colId, property);
		    }
		};
		
		dgEmployeeAgent.setImmediate(false);
		dgEmployeeAgent.setWidth("100.0%");
		dgEmployeeAgent.setHeight("100.0%");
		mainLayout.addComponent(dgEmployeeAgent);
		mainLayout.setExpandRatio(dgEmployeeAgent, 1.0f);
		
		return mainLayout;
	}

	@Override
	public void filterButtonClick(BoxToolbar.ClickNavigationEvent event) {
		dgEmployeeAgent.setFilterBarVisible();
		
	}

	@Override
	public void printButtonClick(BoxToolbar.ClickNavigationEvent event) {
		try {
			dgEmployeeAgent.print("Maestro Empleados");
		}
		catch (Exception e) {
			throw new RuntimeException("¡No se pudo imprimir el informe!", e);
		}
		
	}

	@Override
	public void importButtonClick(BoxToolbar.ClickNavigationEvent event) {
		List<EmployeeAgent> employeeAgents = new ArrayList<EmployeeAgent>();
		
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
						
						Excel2Entity excel2Entity = new Excel2Entity(context, addressService, employeeAgentTypeService, employeeAgentStatusService, row);
						
						try {
							employeeAgents.add(excel2Entity.parse());
						} catch (ParseException e) {
							throw new RuntimeException("¡No se pudo parsear el registro excel!", e);
						} catch (Exception e) {
							throw new RuntimeException("¡No se pudo parsear el registro excel!", e);
						}
						
					}
					
					try {
						employeeAgentKnowledgeService.importEmployeeAgent(context.getUser().getActiveOrganization(), employeeAgents);
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
		dgEmployeeAgent.setColumnHeaders(new String[] { getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.employeeAgentId"), 
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.workNumber"), 
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.tittle"), 
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.type"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.shortName"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.name"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.surname"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.email"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.phone"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.mobile"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.workMobile"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.fax"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.vat"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.facebook"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.comment"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.seniority"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.address"),
				  getI18N().getMessage("com.thingtrack.konekti.view.module.employee.internal.EmployeeAgentView.dgEmployeeAgent.column.status")});
		
	}
	
}
