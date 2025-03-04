package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class SQLUnnestTableSource extends SQLTableSourceImpl
        implements SQLReplaceable {
    private final List<SQLExpr> items = new ArrayList<SQLExpr>();
    protected List<SQLName> columns = new ArrayList<SQLName>();
    private boolean ordinality;
    private SQLExpr offset;

    public SQLUnnestTableSource() {
    }

    @Override
    protected void accept0(SQLASTVisitor v) {
        if (v.visit(this)) {
            acceptChild(v, items);
            acceptChild(v, columns);
            acceptChild(v, offset);
            super.accept0(v);
        }
        v.endVisit(this);
    }

    public List<SQLName> getColumns() {
        return columns;
    }

    public void addColumn(SQLName column) {
        column.setParent(this);
        this.columns.add(column);
    }

    public boolean isOrdinality() {
        return ordinality;
    }

    public void setOrdinality(boolean ordinality) {
        this.ordinality = ordinality;
    }

    public List<SQLExpr> getItems() {
        return items;
    }

    public void addItem(SQLExpr item) {
        item.setParent(this);
        this.items.add(item);
    }

    public void setItem(int i, SQLExpr item) {
        this.items.set(i, item);
    }

    public SQLExpr getOffset() {
        return offset;
    }

    public void setOffset(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.offset = x;
    }

    public SQLUnnestTableSource clone() {
        SQLUnnestTableSource x = new SQLUnnestTableSource();

        for (SQLExpr item : items) {
            SQLExpr item2 = item.clone();
            item2.setParent(x);
            x.items.add(item2);
        }

        for (SQLName column : columns) {
            SQLName c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }

        x.alias = alias;

        if (offset != null) {
            x.setOffset(offset);
        }

        return x;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) == expr) {
                target.setParent(this);
                items.set(i, target);
                return true;
            }
        }

        if (target instanceof SQLName) {
            SQLName targetName = (SQLName) target;
            for (int i = 0; i < columns.size(); i++) {
                if (columns.get(i) == expr) {
                    target.setParent(this);
                    columns.set(i, targetName);
                    return true;
                }
            }
        }
        return false;
    }
}
