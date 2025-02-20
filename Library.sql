CREATE TABLE `Role` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Employee` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `username` varchar(255) UNIQUE NOT NULL,
  `passord` varchar(255) NOT NULL,
  `roleId` bigint NOT NULL,
  `phone` varchar(20) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Customer` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Author` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Bookshelf` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Category` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Book` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `categoryId` bigint NOT NULL,
  `authorId` bigint NOT NULL,
  `publisherId` bigint NOT NULL,
  `bookshelfId` bigint NOT NULL,
  `quantity` int NOT NULL,
  `yearOfpublication` year,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Publisher` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255),
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `Borrow` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `employeeId` bigint NOT NULL,
  `customerId` bigint NOT NULL,
  `status` ENUM ('Borrowed', 'Returned', 'Overdue') NOT NULL DEFAULT 'Borrowed',
  `duedate` datetime NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `BorrowDetails` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `borrowId` bigint NOT NULL,
  `bookId` bigint NOT NULL,
  `quatity` int NOT NULL DEFAULT 1
);

CREATE TABLE `Supplier` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatedAt` datetime DEFAULT null
);

CREATE TABLE `PurchaseOrders` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `supplierId` bigint NOT NULL,
  `employeeId` bigint NOT NULL,
  `status` ENUM ('Pending', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending',
  `totalAmount` decimal(10,2) NOT NULL DEFAULT 0,
  `createdAt` datetime NOT NULL DEFAULT (now()),
  `updatdeAt` datetime DEFAULT null
);

CREATE TABLE `PurchaseOrderDetails` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `purchaseOrderId` bigint NOT NULL,
  `bookId` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `unitPrice` decimal(10,2) NOT NULL,
  `SubTotal` decimal(10,2) NOT NULL
);

ALTER TABLE `Employee` ADD FOREIGN KEY (`roleId`) REFERENCES `Role` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`categoryId`) REFERENCES `Category` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`authorId`) REFERENCES `Author` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`publisherId`) REFERENCES `Publisher` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`bookshelfId`) REFERENCES `Bookshelf` (`id`);

ALTER TABLE `Borrow` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`);

ALTER TABLE `Borrow` ADD FOREIGN KEY (`customerId`) REFERENCES `Customer` (`id`);

ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`borrowId`) REFERENCES `Borrow` (`id`);

ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`);

ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`supplierId`) REFERENCES `Supplier` (`id`);

ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`);

ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`purchaseOrderId`) REFERENCES `PurchaseOrders` (`id`);

ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`);