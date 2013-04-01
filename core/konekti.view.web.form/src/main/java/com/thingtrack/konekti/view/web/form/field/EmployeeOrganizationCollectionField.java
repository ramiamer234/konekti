package com.thingtrack.konekti.view.web.form.field;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.vaadin.addon.customfield.CustomField;

import com.thingtrack.konekti.domain.EmployeeAgent;
import com.thingtrack.konekti.domain.Organization;
import com.thingtrack.konekti.service.api.EmployeeAgentService;
import com.thingtrack.konekti.service.api.OrganizationService;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class EmployeeOrganizationCollectionField extends CustomField {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Table organizationsTable;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	private BindingSource<Organization> organizationsBindingSource;
	
	private List<Organization> assignedOrganizations;
	
	private EmployeeAgentService employeeAgentService;
	private OrganizationService organizationService;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public EmployeeOrganizationCollectionField() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		
		//Retrieve enterprise services
		getServices();
		
		organizationsBindingSource = new BindingSource<Organization>(Organization.class);
		
		organizationsTable.setContainerDataSource(organizationsBindingSource);
		organizationsTable.addGeneratedColumn(EmployeeAgentAssignmentColumn.EMPLOYEE_AGENT_COLUMN_ID, new EmployeeAgentAssignmentColumn());
		organizationsTable.setVisibleColumns(new String[]{"cif", "description", EmployeeAgentAssignmentColumn.EMPLOYEE_AGENT_COLUMN_ID});
		
	}
	
	@Override
	public void setPropertyDataSource(Property newDataSource) {

		if(newDataSource.getValue() instanceof List){
			
			List<Organization> organizations = (List<Organization>) newDataSource.getValue();
			
			//Already organziations assigned to parent vehicle
			assignedOrganizations = organizations;
			
			//Clean first the biding source
			organizationsBindingSource.removeAllItems();
			try {
				//Add all available organizations 
				organizationsBindingSource.addAll(organizationService.getAll());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		super.setPropertyDataSource(newDataSource);
	}
	
	@Override
	public Class<?> getType() {

		if(getPropertyDataSource() instanceof Property)
			return getPropertyDataSource().getType();
		
		return List.class;
	}
	
	@Override
	public Object getValue() {		
		return assignedOrganizations;
		
	}
	
	public EmployeeAgent getSelectedValue() {		
		return (EmployeeAgent)organizationsTable.getValue();
		
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
		
		// Organizationstable
		organizationsTable = new Table();
		organizationsTable.setSelectable(true);
		organizationsTable.setImmediate(true);
		organizationsTable.setWidth("100.0%");
		organizationsTable.setHeight("100.0%");
		mainLayout.addComponent(organizationsTable);
		mainLayout.setExpandRatio(organizationsTable, 1.0f);
		
		return mainLayout;
	}
	
	@SuppressWarnings("unchecked")
	public void getServices(){
		
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		
		ServiceReference employeeAgentServiceReference = bundleContext.getServiceReference(EmployeeAgentService.class.getName());
		employeeAgentService =  EmployeeAgentService.class.cast(bundleContext.getService(employeeAgentServiceReference));
		
		ServiceReference organizationServiceReference = bundleContext.getServiceReference(OrganizationService.class.getName());
		organizationService = OrganizationService.class.cast(bundleContext.getService(organizationServiceReference));
	}
	
	
	private class EmployeeAgentAssignmentColumn implements Table.ColumnGenerator{

		static final String EMPLOYEE_AGENT_COLUMN_ID = "employee-assignment";

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {
			
			CheckBox assignmentColumn = new CheckBox();
			
			final Organization organizationItem = (Organization) itemId;
			
			assignmentColumn.setValue(assignedOrganizations.contains(organizationItem));
				
			assignmentColumn.addListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {				
					boolean assignmentValue = (Boolean) event.getProperty().getValue();
					
					if(assignmentValue)
						assignedOrganizations.add(organizationItem);
					
					else assignedOrganizations.remove(organizationItem);
					
				}
				
			});
			
			return assignmentColumn;
		}
		
		
	}
	
}
