package home.app.direct.vaadin.components.view.grid;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import home.app.direct.vaadin.components.view.buttons.ExportButton;
import home.app.direct.vaadin.components.view.form.PersistEntityEvent;
import home.app.direct.vaadin.components.view.grid.container.EntityContainerFacade;
import home.app.direct.vaadin.components.view.grid.pagination.PaginationLayout;
import home.app.direct.vaadin.components.view.window.WindowForm;
import home.app.direct.vaadin.components.view.buttons.DeleteButton;
import home.app.direct.vaadin.components.view.buttons.DeleteListener;
import home.app.direct.vaadin.components.view.form.FormEvent;
import home.app.direct.vaadin.components.view.grid.fields.EntityFieldGroup;
import home.app.direct.vaadin.components.view.grid.fields.EntityFieldGroupFactory;

import java.util.List;

public class GridFooter<T> extends HorizontalLayout{

    private EntityContainerFacade<T> entityContainer;
    private EntityFieldGroupFactory<T> entityFieldGroupFactory;
    private JPAGrid<T> grid;
    private EntityFieldGroup<T> entityFieldGroup;
    private boolean writeRights;
    private boolean deleteAllowed;
    private boolean exportAllowed;
    private boolean addAllowed;
    private boolean popupForm;
    private String formLabel;
    private boolean hasPaginationLayout;
    private List<DeleteListener<T>> deleteListeners;
    private List<FormEvent> formEventList;

    private Button addButton;
    private PersistEntityEvent<T> persistEntityEvent;
    private DeleteButton<T> deleteButton;

    public GridFooter(){

    }
    public GridFooter init(){
        entityFieldGroup = entityFieldGroupFactory.buildNewEntityFieldGroup();
        setSizeFull();
        VerticalLayout left = new VerticalLayout();
        left.setSizeFull();
        left.setDefaultComponentAlignment(Alignment.TOP_LEFT);
        if(writeRights) {
            HorizontalLayout writeLayout = new HorizontalLayout();
            writeLayout.setMargin(new MarginInfo(true, true, true, true));
            writeLayout.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
            addButton = buildAddButton();
            deleteButton = buildDeleteButton();
            if(addButton!=null) {
                writeLayout.addComponent(addButton);
            }
            if (deleteButton != null) {
                writeLayout.addComponent(deleteButton);
            }
            left.addComponent(writeLayout);
        }
        if(hasPaginationLayout &&
                   entityContainer.getPageSize() < entityContainer.getTotalSize()){
            left.addComponent(new PaginationLayout<>(entityContainer));
        }
        addComponent(left);
        VerticalLayout right = new VerticalLayout();
        right.setWidth(400, Unit.PIXELS);
        right.setMargin(new MarginInfo(true, true, true, true));
        if(exportAllowed) {
            right.addComponent(getExporterComponent());
        }
        addComponent(right);
        setComponentAlignment(right, Alignment.TOP_RIGHT);
        return this;
    }

    private Component getExporterComponent(){
        return new ExportButton(entityContainer, entityFieldGroupFactory);
    }


    private DeleteButton<T> buildDeleteButton(){
        if(deleteAllowed){
            deleteButton = new DeleteButton<T>(entityContainer, grid);
            deleteButton.setListeners(deleteListeners);
        }
        return deleteButton;
    }

    /*********** FORM AND IMPORT FIELDS SPECIFIC ***********/

    private Button buildAddButton() {
        if(addAllowed) {
            addButton = new Button("Add");
            addButton.addClickListener(p->{
                    if (p.getSource() == addButton) {
                        addButton.setVisible(false);
                        if (popupForm) {
                            showWindowForm();
                        } else {
                            grid.setEditorEnabled(true);
                            grid.scrollToEnd();
                            EntityFieldGroup<T> fieldGroup = entityFieldGroupFactory.buildNewEntityFieldGroup();
                            grid.setEditorFieldGroup(fieldGroup);
                            entityContainer.save(fieldGroup.getEntity());
                            grid.editItem(fieldGroup.getEntity());
                        }
                    }
            });
        }
        return addButton;
    }

    private void showWindowForm(){
        WindowForm<T> windowForm = new WindowForm<T>(formLabel, entityContainer, entityFieldGroupFactory.buildNewEntityFieldGroup()){
            @Override
            protected Field customizeFormField(String fieldId, Field field) {
                return GridFooter.this.customizeFormField(fieldId, field);
            }

            @Override
            protected PersistEntityEvent persistEvent() {
                return GridFooter.this.persistEvent();
            }
        };
        getUI().addWindow(windowForm);
        windowForm.addCloseListener(p->addButton.setVisible(true));
        windowForm.addActionListeners(formEventList);
    }

    private PersistEntityEvent persistEvent() {
        return persistEntityEvent;
    }

    protected Field customizeFormField(String fieldId, Field field){
        return field;
    }

    public GridFooter entityContainer(EntityContainerFacade<T> entityContainer) {
        this.entityContainer = entityContainer;
        return this;
    }

    public GridFooter entityFieldGroupFactory(EntityFieldGroupFactory<T> entityFieldGroupFactory) {
        this.entityFieldGroupFactory = entityFieldGroupFactory;
        return this;
    }

    public GridFooter grid(JPAGrid<T> grid) {
        this.grid = grid;
        return this;
    }


    public GridFooter writeRights(boolean writeRights) {
        this.writeRights = writeRights;
        return this;
    }

    public GridFooter deleteAllowed(boolean deleteAllowed) {
        this.deleteAllowed = deleteAllowed;
        return this;
    }


    public GridFooter exportAllowed(boolean exportAllowed) {
        this.exportAllowed = exportAllowed;
        return this;
    }


    public GridFooter addAllowed(boolean addAllowed) {
        this.addAllowed = addAllowed;
        return this;
    }

    public GridFooter popupForm(boolean popupForm) {
        this.popupForm = popupForm;
        return this;
    }

    public GridFooter formLabel(String formLabel) {
        this.formLabel = formLabel;
        return this;
    }

    public GridFooter formEventList(List<FormEvent> formEventList) {
        this.formEventList = formEventList;
        return this;
    }

    public DeleteButton<T> getDeleteButton() {
        return deleteButton;
    }

    public GridFooter deleteListeners(List<DeleteListener<T>> listeners) {
        this.deleteListeners = listeners;
        return this;
    }

    public GridFooter persistEntityEvent(PersistEntityEvent<T> persistEntityEvent) {
        this.persistEntityEvent = persistEntityEvent;
        return this;
    }
    public GridFooter hasPaginationComponent(boolean hasPaginationLayout) {
        this.hasPaginationLayout = hasPaginationLayout;
        return this;
    }
}
