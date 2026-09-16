# Viva Demonstration Guide: UI, Database & Validations

This guide shows exactly how each team member can demonstrate their specific module during the viva. For each member, it provides:
1. **The UI Steps:** How to log in and what to click to perform CRUD (Create, Read, Update, Delete) operations.
2. **The Database Proof:** The exact SQL query to run in MySQL Workbench to prove the data actually changed in the backend.
3. **The Validations:** How the system protects data integrity — what happens when you try to break the rules.

> **Tip:** You can use the `⚡ Fast Switch` dropdown in the top navigation bar to quickly jump between accounts without typing passwords! All manual logins use the password: `password123`.

---

## 1. Order Management (Padmakumara I. M. M. D)

### UI Demonstration:
* **Account:** Log in as **Customer Relations** (`support@test.com`).
* **Action (Read/Update/Delete):** Click on **"Manage Orders"** in the navigation bar. You can view all orders, use the dropdown to change an order status (e.g., from PENDING to PROCESSING), or click "Cancel Order" (which acts as a delete/cancellation).
* *(To Create an order, use the Customer account below).*

### Validations to Demonstrate:
* **Cancel Confirmation:** Clicking "Cancel Order" shows a browser confirmation dialog: *"Are you sure you want to CANCEL this order?"*
* **Delete Confirmation:** Clicking "Delete" shows: *"Are you sure you want to PERMANENTLY DELETE this order?"*
* **Status-Based Deletion:** Only cancelled orders can be permanently deleted. If you try to delete a non-cancelled order, it will be blocked.
* **Refund Block:** If a cancelled order has a pending refund, deletion is blocked with the message: *"You must process the pending refund for this order before it can be deleted."*
* **Authorization Check:** Customers can only cancel their own orders. Trying to cancel another user's order is blocked.

### Database Proof:
In MySQL Workbench, run this query to show the orders being updated/inserted:
```sql
SELECT * FROM orders ORDER BY created_at DESC;
```

---

## 2. Delivery & Driver Management (Nambikandage D. A.)

### UI Demonstration:
* **Account:** Log in as **Delivery Coordinator** (`delivery@test.com`).
* **Action (Driver CRUD):** Click on **"Manage Drivers"**. Click **"Add New Driver"** to create a driver (*Create*). View all drivers in the list (*Read*). Click the **edit icon** to update a driver's name, phone, or vehicle type (*Update*). Click the **delete icon** to remove a driver (*Delete*).
* **Action (Delivery Status Management):** Click on **"Manage Deliveries"**. The page is split into three sections: **Preparing**, **Dispatched**, and **Delivered**. Use the dropdowns to assign a driver and change the status (e.g., PREPARING → DISPATCHED → DELIVERED).

### Validations to Demonstrate:
* **Driver Delete Block:** If a driver has active deliveries assigned, deletion is blocked with the message: *"Cannot delete driver because they have active deliveries or routes assigned."* — Demonstrate by assigning a driver to a delivery, then trying to delete that driver.
* **Delete Confirmation:** Clicking the delete icon on a driver shows a browser confirmation: *"Delete this driver?"*
* **Required Fields:** The driver form requires Name and Phone Number — submitting without them is blocked by the browser.
* **Unique Phone:** Each driver must have a unique phone number. Adding a duplicate will show an error.

### Database Proof:
In MySQL Workbench, run these queries to show drivers and deliveries updating:
```sql
SELECT * FROM drivers;
SELECT * FROM deliveries;
```

---

## 3. Inventory Management (Ananya P. K. O.)

### UI Demonstration:
* **Account:** Log in as **Store Supervisor** (`supervisor@test.com`).
* **Action (Create/Read/Update/Delete):** Click on the red **"Inventory Alerts"** link. You can demonstrate clicking "Resolve" on an alert (which updates/deletes it from the active list). To *Create* an alert, simply go to Manage Products and edit a product so its stock is lower than its Reorder Level!

### Validations to Demonstrate:
* **Dismiss Confirmation:** Clicking "Dismiss" on an alert shows: *"Are you sure you want to dismiss this alert?"*
* **Required Fields:** The restock form requires a restock amount with a minimum value of 1. The manual alert form requires both a product selection and a message.
* **Auto-Alert Creation:** Alerts are automatically created when a product's stock drops below its reorder level — no manual trigger needed.

### Database Proof:
In MySQL Workbench, run this query to show alerts being created and resolved:
```sql
SELECT * FROM inventory_alerts;
```

---

## 4. Finance / Payment Management (Fernando N. A. S.)

### UI Demonstration:
* **Account:** Log in as **Finance Executive** (`finance@test.com`).
* **Action (Create/Read/Update/Delete):** Click on **"Finance Dashboard"**. Scroll down to the "Log New Expense" form to *Create* an expense (e.g., "Electricity Bill", 15000). Use the table on the right to *Edit* or *Delete* existing expenses. Notice the charts update dynamically!

### Validations to Demonstrate:
* **Delete Confirmation:** Clicking "Delete" on an expense shows: *"Are you sure you want to delete this expense?"*
* **Required Fields:** The expense form requires both Description and Amount — submitting without them is blocked.
* **Refund Process Confirmation:** On the "Process Refunds" page, clicking "Process" shows: *"Confirm processing this refund? Funds should be manually sent to the customer before clicking this."*
* **Double Refund Block:** If a refund is already completed (COMPLETED status), trying to process it again is blocked with: *"Refund is already completed."*

### Database Proof:
In MySQL Workbench, run this query to prove the expenses table is updating:
```sql
SELECT * FROM expenses ORDER BY date_added DESC;
```

---

## 5. Product Management (Balasuriya B. M. S. H)

### UI Demonstration:
* **Account:** Log in as **Store Supervisor** (`supervisor@test.com`).
* **Action (Create/Read/Update/Delete):** Click on **"Manage Products"**. Click the blue **"Add New Product"** button to create one. Click **"Edit"** on a product to change its price or stock. Click the red **"Delete"** button to completely erase it from the system. *(Note: If a customer has already ordered the product, clicking Delete will safely "Discontinue" it instead to protect the database!)*

### Validations to Demonstrate:
* **Safe Discontinue:** If a product is linked to existing orders or carts, clicking "Delete" will NOT delete it — it will change its status to **DISCONTINUED** instead, with the message: *"Product is linked to existing data. It has been safely Discontinued instead of permanently deleted."*
* **Delete Confirmation:** Clicking "Delete" shows: *"Are you sure you want to discontinue this product?"* and hard delete shows: *"Are you sure you want to COMPLETELY DELETE this product?"*
* **Required Fields:** The product form requires Name, Category, Price, Unit, Availability, Quantity on Hand, and Reorder Level — all validated by the browser.

### Database Proof:
In MySQL Workbench, run this query to prove the product row was inserted or deleted:
```sql
SELECT * FROM products;
```

---

## 6. Cart Management (Ranasinghe R A I M)

### UI Demonstration:
* **Account:** Log in as **Customer** (`customer@test.com`).
* **Action (Create/Read/Update/Delete):** Click on **"Products"** to browse the catalogue, and click "Add to Cart" (*Create*). Click on **"Cart"** in the top navigation. Change the quantity of an item and click update (*Update*), or click "Remove" to delete it from the cart (*Delete*).

### Validations to Demonstrate:
* **Stock Check on Add:** If a product is unavailable or has insufficient stock, adding to cart is blocked with: *"Product is unavailable or insufficient stock."*
* **Stock Check on Update:** If you try to increase the quantity beyond available stock, it's blocked with: *"Insufficient stock for requested quantity."*
* **Empty Cart Confirmation:** Clicking "Empty Cart" shows: *"Are you sure you want to empty your cart?"*
* **Authorization Check:** Users can only modify their own cart items. The system checks ownership before any update or delete.
* **Empty Cart Checkout Block:** Trying to checkout with an empty cart is blocked.
* **Delivery Address Required:** The checkout form requires a delivery address before proceeding to payment.

### Database Proof:
In MySQL Workbench, run these queries to prove the cart items are being managed in the database:
```sql
SELECT * FROM carts;
SELECT * FROM cart_items;
```

---

### How to use MySQL Workbench during the viva:
1. Double-click on `lanka_fresh_mart` under "SCHEMAS" on the left side so it becomes bold.
2. Paste all the SQL queries above into the large "Query 1" text area.
3. Highlight only the specific line of SQL you want to run.
4. Click the plain yellow **Lightning Bolt icon ⚡** above the text area to execute that specific line and view the data!
