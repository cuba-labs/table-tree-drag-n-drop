package com.company.sales.web.customer;

import com.company.sales.entity.Customer;
import com.company.sales.entity.CustomerGrade;
import com.company.sales.entity.CustomerGradeEntity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.ui.AbstractSelect;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class CustomerBrowse extends AbstractLookup {

    @Inject
    private Tree gradeTree;
    @Inject
    private Table<Customer> customersTable;

    @Inject
    private CollectionDatasource<Customer, UUID> customersDs;

    @Inject
    private Metadata metadata;

    @Override
    public void init(Map<String, Object> params) {
        // Unwrap to a vaadin table ...
        com.vaadin.ui.Table table = customersTable.unwrap(com.vaadin.ui.Table.class);
        // ... to define DragMode
        table.setDragMode(com.vaadin.ui.Table.TableDragMode.ROW);

        gradeTree.setDatasource(createTreeDatasource());

        // Unwrap to a vaadin Tree ...
        com.vaadin.ui.Tree tree = gradeTree.unwrap(com.vaadin.ui.Tree.class);
        // ... to add a DropHandler
        tree.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent event) {
                // Obtain dropped Customer
                UUID sourceItemId = (UUID) event.getTransferable().getData("itemId");
                Customer customer = customersDs.getItemNN(sourceItemId);

                // Obtain selected Grade
                com.vaadin.ui.Tree.TreeTargetDetails targetDetails = (com.vaadin.ui.Tree.TreeTargetDetails) event.getTargetDetails();
                UUID targetItemId = (UUID) targetDetails.getItemIdOver();
                //noinspection unchecked
                CustomerGradeEntity gradeItem = (CustomerGradeEntity) gradeTree.getDatasource().getItemNN(targetItemId);
                CustomerGrade grade = gradeItem.getGrade();

                // Update Customer
                customer.setGrade(grade);
                customersDs.updateItem(customer);
                customersDs.commit();
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                // We want to restrict drops only on an item
                return new And(AbstractSelect.VerticalLocationIs.MIDDLE);
            }
        });
    }

    private HierarchicalDatasource createTreeDatasource() {
        // Datasource Programmatic Creation
        HierarchicalDatasource gradesDs = new DsBuilder()
                .setJavaClass(CustomerGradeEntity.class)
                .setId("gradesDs")
                .buildHierarchicalDatasource();
        gradesDs.setHierarchyPropertyName("parent");

        // Convert Enum values to Entities
        for (CustomerGrade at : CustomerGrade.values()) {
            CustomerGradeEntity gradeEntity = metadata.create(CustomerGradeEntity.class);
            gradeEntity.setGrade(at);
            //noinspection unchecked
            gradesDs.includeItem(gradeEntity);
        }

        return gradesDs;
    }
}