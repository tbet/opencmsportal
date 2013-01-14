package de.eonas.addressbook.model;

import de.eonas.addressbook.genericmodel.LdapSelectableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface I_LazyLdapDataModel<T extends LdapSelectableData> extends SelectableDataModel<T>,Serializable {
    @NotNull
    List<T> load();

    @NotNull
    /**
     * Diese Funktion ist der Kern des Lazy Loadings. Bitte nicht verwenden, Sortieren geht nicht, weil OpenLdap die
     * extension nicht unterstützt. Pagination geht aus dem gleichen Grund nur schlecht.
     */
    List<T> load(int first, int pageSize, @Nullable String sortField, @Nullable SortOrder sortOrder, @Nullable Map<String, String> filters);

    @Override
    Object getRowKey(@NotNull T object);

    @Nullable
    @Override
    T getRowData(String rowKey);

    void save(T object);
}
