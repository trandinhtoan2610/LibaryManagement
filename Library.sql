CREATE TABLE `Role` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL
);

CREATE TABLE `Employee` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `gender` ENUM ('Male', 'Female') NOT NULL DEFAULT 'Male',
  `username` varchar(255) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  `roleId` bigint NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `salary` bigint NOT NULL
);

CREATE TABLE `Reader` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `gender` ENUM ('Male', 'Female') NOT NULL DEFAULT 'Male',
  `phone` varchar(10) NOT NULL,
  `address` varchar(255) NOT NULL
);

CREATE TABLE `Author` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `gender` ENUM ('Male', 'Female') NOT NULL DEFAULT 'Male',
  `phone` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL
);

CREATE TABLE `Category` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL
);

CREATE TABLE `Book` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `categoryId` bigint NOT NULL,
  `authorId` bigint NOT NULL,
  `publisherId` bigint NOT NULL,
  `quantity` int NOT NULL,
  `unitprice` bigint NOT NULL,
  `yeayearOfpublication` year
);

CREATE TABLE `Publisher` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `address` varchar(255) NOT NULL
);

CREATE TABLE `Borrow_in_Sheet` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `employeeId` bigint NOT NULL,
  `readerId` bigint NOT NULL,
  `status` ENUM ('Borrowed', 'Returned', 'Overdue') NOT NULL DEFAULT 'Borrowed',
  `borrowedDate` datetime NOT NULL DEFAULT (now()),
  `duedate` datetime NOT NULL,
  `actualReturnDate` datetime NOT NULL
);

CREATE TABLE `BorrowDetails` (
  `borrowId` bigint NOT NULL,
  `bookId` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `substatus` ENUM ('Not_Returned', 'Returned') NOT NULL DEFAULT 'Not_Returned',
  PRIMARY KEY (`borrowId`, `bookId`)
);

CREATE TABLE `Supplier` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `address` varchar(255) NOT NULL
);

CREATE TABLE `PurchaseOrders` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `supplierId` bigint NOT NULL,
  `employeeId` bigint NOT NULL,
  `status` ENUM ('Pending', 'Completed', 'Cancelled') NOT NULL DEFAULT 'Pending',
  `totalAmount` decimal(10,2) NOT NULL DEFAULT 0,
  `buyDate` datetime NOT NULL DEFAULT (now())
);

CREATE TABLE `PurchaseOrderDetails` (
  `purchaseOrderId` bigint NOT NULL,
  `bookId` bigint NOT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `unitPrice` decimal(10,2) NOT NULL,
  `SubTotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`purchaseOrderId`, `bookId`)
);

CREATE TABLE `Penalty` (
  `penaltyId` bigint NOT NULL,
  `penaltyDate` datetime NOT NULL,
  `borrowId` bigint NOT NULL,
  `status` ENUM ('paid', 'not_paid') NOT NULL DEFAULT (not_paid),
  `totalamount` decimal(10,2) NOT NULL
);

CREATE TABLE `PenaltyDetails` (
  `penaltyId` bigint NOT NULL,
  `punish` varchar(255) NOT NULL,
  `subamount` demical(10,2) NOT NULL
);

ALTER TABLE `Employee` ADD FOREIGN KEY (`roleId`) REFERENCES `Role` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`categoryId`) REFERENCES `Category` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`authorId`) REFERENCES `Author` (`id`);

ALTER TABLE `Book` ADD FOREIGN KEY (`publisherId`) REFERENCES `Publisher` (`id`);

ALTER TABLE `Borrow_in_Sheet` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`);

ALTER TABLE `Borrow_in_Sheet` ADD FOREIGN KEY (`readerId`) REFERENCES `Reader` (`id`);

ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`borrowId`) REFERENCES `Borrow_in_Sheet` (`id`);

ALTER TABLE `BorrowDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`);

ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`supplierId`) REFERENCES `Supplier` (`id`);

ALTER TABLE `PurchaseOrders` ADD FOREIGN KEY (`employeeId`) REFERENCES `Employee` (`id`);

ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`purchaseOrderId`) REFERENCES `PurchaseOrders` (`id`);

ALTER TABLE `PurchaseOrderDetails` ADD FOREIGN KEY (`bookId`) REFERENCES `Book` (`id`);

ALTER TABLE `Penalty` ADD FOREIGN KEY (`borrowId`) REFERENCES `Borrow_in_Sheet` (`id`);

ALTER TABLE `PenaltyDetails` ADD FOREIGN KEY (`penaltyId`) REFERENCES `Penalty` (`penaltyId`);
