package de.eonas.addressbook.genericmodel;

import de.eonas.addressbook.model.I_LazyLdapDataModel;
import de.eonas.addressbook.model.Person;
import org.apache.commons.beanutils.BeanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class MockedLazyLdapDataModel extends LazyDataModel<Person> implements I_LazyLdapDataModel<Person> {

    private static final Logger LOG = LoggerFactory.getLogger(MockedLazyLdapDataModel.class);
    private HashMap<String, Person> m_map;

    public MockedLazyLdapDataModel(Class<Person> clazz, @SuppressWarnings("SameParameterValue") String objectClass) {
        m_map = new HashMap<String, Person>();
    }

    @NotNull
    public List<Person> load() {
        return load(0, Integer.MAX_VALUE, null, null, null);
    }

    @NotNull
    @Override
    /**
     * Diese Funktion ist der Kern des Lazy Loadings. Bitte nicht verwenden, Sortieren geht nicht, weil OpenLdap die
     * extension nicht unterstützt. Pagination geht aus dem gleichen Grund nur schlecht.
     */
    public List<Person> load(int first, int pageSize, @Nullable final String sortField, @Nullable final SortOrder sortOrder, @Nullable Map<String, String> filters) {
        Collection<Person> collection = m_map.values();
        ArrayList<Person> list = new ArrayList<Person>(collection);
        Collections.sort(list, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                int cmp = sanitize(o1, o2);
                if (cmp != Integer.MAX_VALUE) return cmp;

                String v1 = null;
                try {
                    v1 = BeanUtils.getProperty(o1, sortField);
                    String v2 = BeanUtils.getProperty(o2, sortField);
                    cmp = sanitize(v1, v2);
                    if (cmp != Integer.MAX_VALUE) return cmp;

                    if (sortOrder != null || sortOrder.equals(SortOrder.ASCENDING)) {
                        return v1.compareTo(v2);
                    } else {
                        return v2.compareTo(v1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private int sanitize(Object o1, Object o2) {
                if (o1 == null && o2 == null) return 0;
                if (o1 == null) return -1;
                if (o2 == null) return 1;
                return Integer.MAX_VALUE;
            }
        });

        return list.subList(first, first + pageSize); // mock doesn't support filters, however
    }

    @Override
    public Object getRowKey(@NotNull Person object) {
        return object.getDn();
    }

    @Nullable
    @Override
    public Person getRowData(String rowKey) {
        return m_map.get(rowKey);
    }

    public void save(Person object) {
        m_map.put(object.getDn(), object);
    }
}
