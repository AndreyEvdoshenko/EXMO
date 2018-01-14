CREATE TABLE exmoOrders (
  id CHAR(64),
  status CHAR(10),
  profit char(10),
  pair CHAR(7),
  type CHAR(30),
  quantity CHAR(64),
  price CHAR(64),
  medium_price CHAR(64),
  exclusion_medium CHAR(64),
  exclusion_buy CHAR(64),
  updated TIMESTAMP
);

