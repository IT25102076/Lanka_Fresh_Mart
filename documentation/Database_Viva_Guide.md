# 🎯 Lanka Fresh Mart — Viva Cheat Sheet

> 💡 **Quick Login:** Use `⚡ Fast Switch` dropdown in the navbar — no passwords needed!  
> 🔑 **Manual Login Password:** `password123` (for all accounts)

---

## 👤 Quick Account Reference

| Role | Email | Modules |
|:-----|:------|:--------|
| 🛒 Customer | `customer@test.com` | Cart, Orders, Checkout, Support |
| 📋 Customer Relations | `support@test.com` | Manage Orders, Manage Support |
| 🚚 Delivery Coordinator | `delivery@test.com` | Manage Deliveries, Manage Drivers |
| 💰 Finance Executive | `finance@test.com` | Finance Dashboard, Process Refunds |
| 🏪 Store Supervisor | `supervisor@test.com` | Manage Products, Inventory Alerts |
| 👑 Operations Manager | `operations@test.com` | All of the above |

---

## 1️⃣ Order Management — Padmakumara I. M. M. D

> 🔐 Login: **Customer Relations** → `support@test.com`  
> 📍 Navigate: **Manage Orders**

### 🖱️ CRUD Steps

| Action | How |
|:-------|:----|
| **C** reate | Use Customer account → add items → checkout → pay |
| **R** ead | View all orders in the table |
| **U** pdate | Change status dropdown (PENDING → PROCESSING → SHIPPED) → click Save |
| **D** elete | Click **Cancel Order** → then click **Delete** on cancelled orders |

### 🛡️ Validations

| What to Try | What Happens |
|:------------|:-------------|
| Click **Cancel Order** | ⚠️ Confirmation: *"Are you sure you want to CANCEL this order?"* |
| Click **Delete** on active order | ❌ Blocked — *"Only cancelled orders can be permanently deleted."* |
| Delete order with pending refund | ❌ Blocked — *"You must process the pending refund before it can be deleted."* |
| Cancel another user's order | ❌ Blocked — *"You are not authorized to cancel this order."* |

### 💾 SQL Proof
```sql
SELECT * FROM orders ORDER BY created_at DESC;
```

---

## 2️⃣ Delivery & Driver Management — Nambikandage D. A.

> 🔐 Login: **Delivery Coordinator** → `delivery@test.com`  
> 📍 Navigate: **Manage Drivers** + **Manage Deliveries**

### 🖱️ CRUD Steps — Drivers

| Action | How |
|:-------|:----|
| **C** reate | Click **Add New Driver** → fill Name, Phone, Vehicle Type → Save |
| **R** ead | View all drivers in the list with assignment counts |
| **U** pdate | Click ✏️ edit icon → change details → Update Driver |
| **D** elete | Click 🗑️ delete icon → confirm |

### 🖱️ Delivery Status Flow

```
📦 PREPARING  →  🚛 DISPATCHED  →  ✅ DELIVERED
```

> Each status has its own section on the page. Use the **status dropdown** + **driver dropdown** → click **Save**.

### 🛡️ Validations

| What to Try | What Happens |
|:------------|:-------------|
| Delete driver **with** active deliveries | ❌ Blocked — *"Cannot delete driver because they have active deliveries assigned."* |
| Delete driver **without** deliveries | ✅ Deleted successfully |
| Submit driver form with empty name | ❌ Browser blocks — fields are required |
| Add driver with duplicate phone | ❌ Error — phone must be unique |

### 💾 SQL Proof
```sql
SELECT * FROM drivers;
SELECT * FROM deliveries;
```

---

## 3️⃣ Inventory Management — Ananya P. K. O.

> 🔐 Login: **Store Supervisor** → `supervisor@test.com`  
> 📍 Navigate: **Inventory Alerts** (red link in navbar)

### 🖱️ CRUD Steps

| Action | How |
|:-------|:----|
| **C** reate (auto) | Edit a product → set stock **below** reorder level → alert auto-created! |
| **C** reate (manual) | Use "Create Manual Alert" form at the bottom |
| **R** ead | View all active alerts in the table |
| **U** pdate | Click **Resolve** → enter restock amount → stock gets replenished |
| **D** elete | Click the ✕ dismiss button on any alert |

### 🛡️ Validations

| What to Try | What Happens |
|:------------|:-------------|
| Click **Dismiss** on an alert | ⚠️ Confirmation: *"Are you sure you want to dismiss this alert?"* |
| Submit restock with amount 0 | ❌ Browser blocks — minimum value is 1 |
| Submit manual alert without message | ❌ Browser blocks — fields are required |

### 💾 SQL Proof
```sql
SELECT * FROM inventory_alerts;
```

---

## 4️⃣ Finance / Payment Management — Fernando N. A. S.

> 🔐 Login: **Finance Executive** → `finance@test.com`  
> 📍 Navigate: **Finance Dashboard** + **Process Refunds**

### 🖱️ CRUD Steps — Expenses

| Action | How |
|:-------|:----|
| **C** reate | Scroll to "Log New Expense" → enter description + amount → Save |
| **R** ead | View expense table + charts update dynamically |
| **U** pdate | Click ✏️ on an expense → edit inline → Save |
| **D** elete | Click 🗑️ on an expense → confirm |

### 🛡️ Validations

| What to Try | What Happens |
|:------------|:-------------|
| Delete an expense | ⚠️ Confirmation: *"Are you sure you want to delete this expense?"* |
| Submit expense without description | ❌ Browser blocks — fields are required |
| Process a refund | ⚠️ Confirmation: *"Confirm processing this refund?"* |
| Process an **already completed** refund | ❌ Blocked — *"Refund is already completed."* |

### 💾 SQL Proof
```sql
SELECT * FROM expenses ORDER BY date_added DESC;
```

---

## 5️⃣ Product Management — Balasuriya B. M. S. H

> 🔐 Login: **Store Supervisor** → `supervisor@test.com`  
> 📍 Navigate: **Manage Products**

### 🖱️ CRUD Steps

| Action | How |
|:-------|:----|
| **C** reate | Click **Add New Product** → fill all fields → Save |
| **R** ead | View all products in the table |
| **U** pdate | Click **Edit** → change price, stock, etc. → Save |
| **D** elete | Click **Delete** → if linked to orders, it **Discontinues** instead |

### 🛡️ Validations

| What to Try | What Happens |
|:------------|:-------------|
| Delete product **with** existing orders | 🔄 Auto-Discontinue — *"Product is linked to existing data. It has been safely Discontinued."* |
| Delete product **without** orders | ✅ Permanently deleted |
| Hard delete confirmation | ⚠️ *"Are you sure you want to COMPLETELY DELETE this product?"* |
| Submit product form with empty name | ❌ Browser blocks — all fields required |

### 💾 SQL Proof
```sql
SELECT * FROM products;
```

---

## 6️⃣ Cart Management — Ranasinghe R A I M

> 🔐 Login: **Customer** → `customer@test.com`  
> 📍 Navigate: **Products** → **Cart**

### 🖱️ CRUD Steps

| Action | How |
|:-------|:----|
| **C** reate | Browse Products → click **Add to Cart** |
| **R** ead | Click **Cart** in navbar → view all items |
| **U** pdate | Change quantity → click **Update** |
| **D** elete | Click **Remove** on an item, or **Empty Cart** for all |

### 🛡️ Validations

| What to Try | What Happens |
|:------------|:-------------|
| Add out-of-stock product | ❌ Blocked — *"Product is unavailable or insufficient stock."* |
| Update quantity beyond stock | ❌ Blocked — *"Insufficient stock for requested quantity."* |
| Click **Empty Cart** | ⚠️ Confirmation: *"Are you sure you want to empty your cart?"* |
| Checkout with empty cart | ❌ Blocked — *"Cart is empty."* |
| Checkout without delivery address | ❌ Browser blocks — address is required |

### 💾 SQL Proof
```sql
SELECT * FROM carts;
SELECT * FROM cart_items;
```

---

## 🖥️ How to Use MySQL Workbench

1. Double-click `lanka_fresh_mart` under **SCHEMAS** (left panel) so it becomes **bold**
2. Paste the SQL queries into the **Query** tab
3. **Highlight** only the line you want to run
4. Click the ⚡ **Lightning Bolt** icon to execute and see results!
