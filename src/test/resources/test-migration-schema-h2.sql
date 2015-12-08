 create table Company (
    id bigint not null,
    country varchar(255),
    email varchar(255),
    name varchar(255),
    phoneNumber varchar(255),
    uuid varchar(255),
    website varchar(255),
    primary key (id)
    );
 create table Subscription (
    id bigint not null,
    company_id bigint,
    subscriptionOrder_id bigint,
    user_id bigint,
    primary key (id)
    );
 create table SubscriptionOrder (
    id bigint not null,
    editionCode varchar(255),
    pricingDuration varchar(255),
    primary key (id)
    );
 create table users (
    id bigint not null,
    email varchar(255),
    firstName varchar(255),
    language varchar(255),
    lastName varchar(255),
    openId varchar(255),
    uuid varchar(255),
    primary key (id)
    );
 alter table Subscription add constraint FKf5ksl1jtr821fm8vk2knsq9s9 foreign key (company_id) references Company
 alter table Subscription add constraint FK5t3wqi9syqt7arn5m1e9yw477 foreign key (subscriptionOrder_id) references SubscriptionOrder
 alter table Subscription add constraint FK9vboumocathwyxuk02qlh8xsy foreign key (user_id) references users
