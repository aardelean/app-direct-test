package home.app.direct.orders;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public abstract class SimpleGrid<T> extends Grid {

	protected BeanItemContainer<T> container;
	private Class clazz;

	public SimpleGrid(String title, Class clazz) {
		super(title);
		this.clazz = clazz;
		container = new BeanItemContainer<T>(clazz);
		for (String columnName : getColumnNames()) {
			if (columnName.contains(".")) {
				container.addNestedContainerProperty(columnName);
			}
			Column column = addColumn(columnName);
			customizeColumn(column);
		}
		setContainerDataSource(container);
	}

    public SimpleGrid start(){
        List resultList = new ArrayList<>();
        Collections.addAll(resultList, getRepository().findAll());
        init(resultList);
        return this;
    }

	public void init(Collection<T> list) {
		container.removeAllItems();
		container.addAll(list);
	}

    protected abstract CrudRepository<T,?> getRepository();

	protected void customizeColumn(Column column) {

	}

	protected abstract Set<String> getColumnNames();

}
