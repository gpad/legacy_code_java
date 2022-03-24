import java.util.ArrayList;
import java.util.List;

class Table {
    private final int _id;
    private final int _size;
    private final int _room;

    Table(int id, int size, int room) {
        _id = id;
        _size = size;
        _room = room;
    }

    int id() {
        return _id;
    }

    int size() {
        return _size;
    }

    int room() {
        return _room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (_id != table._id) return false;
        if (_size != table._size) return false;
        return _room == table._room;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + _size;
        result = 31 * result + _room;
        return result;
    }
}

class Tables {
    private static Tables _instance = null;
    private List<Table> _tables = new ArrayList<>();
    private List<Table> _free = new ArrayList<>();
    private List<Table> _assigned = new ArrayList<>();

    static Tables getInstance() {
        if (_instance == null) {
            _instance = new Tables();
        }
        return _instance;

    }

    void AddTables(List<Table> tables, boolean free) {
        for (var t1 : tables) {
            boolean find = false;
            for (var t2 : _tables) {
                if (t1.equals(t2)) {
                    find = true;
                }
            }
            if (!find) {
                _tables.add(t1);
                if (free) {
                    _free.add(t1);
                } else {
                    _assigned.add(t1);
                }
            }
        }

    }

    boolean isValid(int id) {
        for (var t : _tables) {
            if (t.id() == id) {
                return false;
            }
        }
        return true;

    }

    Table getTablesfor(int size) {
        return getTablesfor(size, -1);
    }

    Table getTablesfor(int size, int room) {
        for (var t : _free) {
            if (t.size() >= size && t.room() == room || room == -1) {
                return t;
            }
        }
        return new Table(-1, -1, -1);

    }

    boolean assignTable(int id) {
        boolean found = false;
        for (var t : _tables) {
            if (t.id() == id) {
                found = true;
            }
        }

        if (found) {
            for (var t : _assigned) {
                if (t.id() == id) {
                    return false;
                }
            }

            int index_free = -1;
            for (int i = 0; i < _free.size(); ++i) {
                if (_free.get(i).id() == id) {
                    index_free = i;
                }
            }

            if (index_free != -1) {
                _assigned.add(_free.get(index_free));
                _free.remove(index_free);
                return true;
            }
            return false;
        }
		return true;
    }

    boolean freeTable(int id) {
        boolean found = false;
        for (var t : _tables) {
            if (t.id() == id) {
                found = true;
            }
        }
        if (found) {
            for (var t : _free) {
                if (t.id() == id) {
                    return false;
                }
            }

            int assigned_index = -1;
            for (int i = 0; i < _assigned.size(); ++i) {
                if (_assigned.get(i).id() == id) {
                    assigned_index = i;
                }
            }

            if (assigned_index != -1) {
                _free.add(_free.get(assigned_index));
                _assigned.remove(assigned_index);
                return true;
            }
            return false;
        }
        return true;
    }
}
