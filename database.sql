
-- ======================================================================
CREATE TABLE public.customers
(
  id integer NOT NULL DEFAULT nextval('customers_id_seq'::regclass),
  name character varying(50) NOT NULL,
  address character varying(50) NOT NULL,
  created_on timestamp without time zone NOT NULL,
  username character varying(50) NOT NULL,
  balance integer,
  CONSTRAINT customers_pkey PRIMARY KEY (id),
  CONSTRAINT customers_username_key UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.customers
  OWNER TO postgres;

-- =============================Operators Table========================
-- Table: public.operators

-- DROP TABLE public.operators;

CREATE TABLE public.operators
(
  id integer NOT NULL DEFAULT nextval('"operators_id_seq"'::regclass),
  name character varying(20) NOT NULL,
  username character varying(50) NOT NULL,
  password character varying(50) NOT NULL,
  CONSTRAINT "operators_pkey" PRIMARY KEY (id),
  CONSTRAINT "operators_username_key" UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.operators
  OWNER TO postgres;


-- ====================================================================
CREATE TABLE public.invoices
(
   id serial,
   customer_id integer NOT NULL,
   description TEXT NOT NULL,
   created_on TIMESTAMP NOT NULL,
   amount integer,
   PRIMARY KEY(id, customer_id),
   FOREIGN KEY(customer_id) REFERENCES customers(id)
) 
WITH (
  OIDS = FALSE
)
;

-- ===================================================================
-- Table: public.invoicelines

-- DROP TABLE public.invoicelines;

CREATE TABLE public.invoicelines
(
  id integer NOT NULL DEFAULT nextval('invoicelines_id_seq'::regclass),
  invoice_id integer,
  description text NOT NULL,
  created_on timestamp without time zone NOT NULL,
  amount integer NOT NULL,
  CONSTRAINT invoicelines_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.invoicelines
  OWNER TO postgres;


-- ==================================================================
CREATE TABLE public.payments
(
    id serial,
    customer_id integer NOT NULL,
    created_on TIMESTAMP NOT NULL,
    amount integer NOT NULL,
    PRIMARY KEY(id, customer_id),
    FOREIGN KEY(customer_id) REFERENCES customers(id)
) 
WITH (
  OIDS = FALSE
)
;

-- ====================================================================
-- Table: public.products

-- DROP TABLE public.products;

CREATE TABLE public.products
(
  product_id integer NOT NULL DEFAULT nextval('customers_id_seq'::regclass),
  product_name character varying(100) NOT NULL,
  amount double precision,
  CONSTRAINT products_pkey PRIMARY KEY (product_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.products
  OWNER TO postgres;




-- ==================================================================