package de.eonas.addressbook.genericmodel;

import de.eonas.addressbook.Dao;
import de.eonas.addressbook.model.LazyDataModel;
import org.jetbrains.annotations.NotNull;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


public class LazyHibernateDataModel<T extends LdapSelectableData> extends org.primefaces.model.LazyDataModel<T> implements LazyDataModel<T> {
    private static final Logger LOG = LoggerFactory.getLogger(LazyHibernateDataModel.class);

    Dao<T> dao;
    Class<T> clazz;

    public LazyHibernateDataModel( Class<T> clazz, Dao<T> dao ) {
        this.clazz = clazz;
        this.dao = dao;
    }

    @NotNull
    @Override
    public List<T> load() {
        return dao.load();
    }

    @Override
    public Object getRowKey(@NotNull T object) {
        return dao.getRowKey(object);
    }

    @Override
    public void save(T object) {
        if ( object.getDn() == null ) {
            object.setDn("" + System.currentTimeMillis());
        }
        dao.save(object);
    }

    @NotNull
    @Override
    public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        return dao.load(first, pageSize, sortField, sortOrder, filters);
    }

}
