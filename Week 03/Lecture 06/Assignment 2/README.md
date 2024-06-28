# Assignment 2: Design Database

This assignment involves designing a database for an invoicing system. You will need to create tables according to the provided schema, set up triggers to enforce specific rules, and insert mock data based on those rules. The focus is on ensuring a clear and detailed explanation of each step, using professional English.

## Requirements and Rules

- **Field Definitions**: Certain fields are calculated by the system and should not be manually edited.
- **Invoice Calculation**: The `amount` field in the `invoice` table should be the sum of the `amount` fields in the related `invoice_detail` entries.
- **Invoice Detail Calculation**: The `amount` field in the `invoice_detail` table should be calculated as `quantity * unit_price`.
- **ID Columns**: All ID columns should preferably use `UUID` for unique identification across systems.

## Table Definitions

### 1. Create Tables

#### Invoice Table

The `invoice` table contains high-level information about each invoice, such as the customer, date, and total amount.

```sql
CREATE TABLE invoice (
    invoice_id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    invoice_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00
);
```

- **`invoice_id`**: A unique identifier for each invoice using `UUID`.
- **`customer_id`**: A unique identifier for the customer, also using `UUID`.
- **`invoice_date`**: The date the invoice was issued.
- **`amount`**: The total amount of the invoice, initialized to `0.00` and updated by triggers.

#### Invoice Detail Table

The `invoice_detail` table holds detailed information about each line item on an invoice, such as item description, quantity, unit price, and total amount for each item.

```sql
CREATE TABLE invoice_detail (
    invoice_detail_id UUID PRIMARY KEY,
    invoice_id UUID REFERENCES invoice(invoice_id),
    item_description VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL
);
```

- **`invoice_detail_id`**: A unique identifier for each invoice detail using `UUID`.
- **`invoice_id`**: A foreign key referencing the `invoice` table, linking each detail to a specific invoice.
- **`item_description`**: A description of the item or service.
- **`quantity`**: The number of units of the item.
- **`unit_price`**: The price per unit of the item.
- **`amount`**: The total amount for this line item, calculated as `quantity * unit_price`.

### 2. Create Triggers

Triggers are essential for maintaining the integrity of the calculated fields. They automatically update the `amount` fields based on changes in the related data.

#### Trigger for Invoice Detail Amount Calculation

This trigger calculates the `amount` for each `invoice_detail` record when it is inserted or updated.

```sql
CREATE TRIGGER calculate_invoice_detail_amount
BEFORE INSERT OR UPDATE ON invoice_detail
FOR EACH ROW
BEGIN
    SET NEW.amount = NEW.quantity * NEW.unit_price;
END;
```

- **`BEFORE INSERT OR UPDATE`**: The trigger is executed before a new record is inserted or an existing record is updated.
- **`NEW.amount`**: The `amount` field is calculated by multiplying `quantity` by `unit_price`.

#### Trigger for Updating Invoice Amount

This trigger updates the `amount` in the `invoice` table whenever a related `invoice_detail` entry is inserted, updated, or deleted.

```sql
CREATE TRIGGER update_invoice_amount
AFTER INSERT OR UPDATE OR DELETE ON invoice_detail
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(10, 2);
    
    -- Calculate the total amount for the related invoice
    IF (TG_OP = 'DELETE') THEN
        SELECT SUM(amount) INTO total FROM invoice_detail WHERE invoice_id = OLD.invoice_id;
        UPDATE invoice SET amount = COALESCE(total, 0.00) WHERE invoice_id = OLD.invoice_id;
    ELSE
        SELECT SUM(amount) INTO total FROM invoice_detail WHERE invoice_id = NEW.invoice_id;
        UPDATE invoice SET amount = COALESCE(total, 0.00) WHERE invoice_id = NEW.invoice_id;
    END IF;
END;
```

- **`AFTER INSERT OR UPDATE OR DELETE`**: The trigger is executed after a record in `invoice_detail` is inserted, updated, or deleted.
- **`total`**: A variable to store the sum of `amount` from the `invoice_detail` records.
- **`COALESCE`**: This function ensures that if there are no `invoice_detail` records, the `amount` is set to `0.00`.
- **`TG_OP`**: A variable that identifies the type of operation (INSERT, UPDATE, DELETE).

### 3. Mock Data

Mock data is inserted to demonstrate the functionality of the tables and triggers. The data follows the specified rules for calculating the fields.

#### Insert Invoice Data

```sql
INSERT INTO invoice (invoice_id, customer_id, invoice_date)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', '2024-06-25'),
    ('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', '2024-06-26');
```

- This inserts two invoices with unique `UUID` identifiers, associated customer IDs, and invoice dates.

#### Insert Invoice Detail Data

```sql
INSERT INTO invoice_detail (invoice_detail_id, invoice_id, item_description, quantity, unit_price)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440000', 'Item A', 2, 100.00),
    ('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440000', 'Item B', 5, 50.00),
    ('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440002', 'Item C', 3, 100.00);
```

- This inserts three `invoice_detail` records with unique `UUID` identifiers, associated invoice IDs, item descriptions, quantities, and unit prices.

### Verify the Data

After inserting the mock data, verify that the triggers have correctly calculated the `amount` fields.

#### Check Invoice Detail Amounts

```sql
SELECT * FROM invoice_detail;
```

It will show each `amount` in `invoice_detail` is calculated as `quantity * unit_price`.

#### Check Invoice Amounts

```sql
SELECT * FROM invoice;
```

It will show each `amount` in the `invoice` table is the sum of the `amount` fields from the corresponding `invoice_detail` entries.

## Summary

### Key Points

1. **Table Creation**: Tables are created with necessary fields and relationships, ensuring referential integrity and proper data organization.
2. **Triggers**: Triggers enforce rules for calculating the `amount` fields, maintaining consistency and data integrity automatically.
3. **Mock Data**: Mock data is inserted to demonstrate the functionality and verify that the triggers and calculations work as expected.
