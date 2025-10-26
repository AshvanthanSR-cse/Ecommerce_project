--
-- PostgreSQL database dump
--

\restrict nkb59Fh1Vgm37viJdv0qVU498ORFDccNvmQPyv5PtpzyNqXScNBSUtcb8igMe7t

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

-- Started on 2025-10-26 14:02:47

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 16663)
-- Name: Tablerecreation; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "Tablerecreation";


ALTER SCHEMA "Tablerecreation" OWNER TO postgres;

--
-- TOC entry 227 (class 1255 OID 16612)
-- Name: assign_user_key(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.assign_user_key() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Generate random 4-digit integer between 1000 and 9999
    NEW.A_user_key := floor(random() * 9000 + 1000)::int;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.assign_user_key() OWNER TO postgres;

--
-- TOC entry 226 (class 1255 OID 16502)
-- Name: generate_product_id(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generate_product_id() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    last_suffix INT := 0;
    prefix INT;
    new_suffix INT;
    final_id BIGINT;
BEGIN
    -- Take manually entered prefix (e.g., 12)
    prefix := NEW.product_id;

    -- Get max suffix for this prefix
    SELECT COALESCE(MAX(product_id) - (prefix * 10000), 0)
    INTO last_suffix
    FROM product_cat
    WHERE (product_id / 10000) = prefix;

    -- Increment suffix
    new_suffix := last_suffix + 1;

    -- Build final id
    final_id := (prefix * 10000) + new_suffix;

    -- Assign to new row
    NEW.product_id := final_id;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.generate_product_id() OWNER TO postgres;

--
-- TOC entry 225 (class 1255 OID 16493)
-- Name: generate_un_key(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generate_un_key() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    rand_key INT;
BEGIN
    rand_key := FLOOR(RANDOM() * 89999999 + 10000000);

    WHILE EXISTS (SELECT 1 FROM acess WHERE un_key = rand_key) LOOP
        rand_key := FLOOR(RANDOM() * 89999999 + 10000000);
    END LOOP;

    NEW.un_key := rand_key;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.generate_un_key() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 218 (class 1259 OID 16454)
-- Name: acess; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.acess (
    username character varying(50) NOT NULL,
    mail_id character varying(50),
    age integer,
    dateof_birth date,
    password character varying(50),
    phone_no bigint DEFAULT 0,
    un_key integer NOT NULL
);


ALTER TABLE public.acess OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16479)
-- Name: acess_un_key_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.acess_un_key_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.acess_un_key_seq OWNER TO postgres;

--
-- TOC entry 4949 (class 0 OID 0)
-- Dependencies: 219
-- Name: acess_un_key_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.acess_un_key_seq OWNED BY public.acess.un_key;


--
-- TOC entry 222 (class 1259 OID 16531)
-- Name: admin_isolated; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admin_isolated (
    mail_id character varying(50),
    username character varying(50),
    password character varying(15),
    aadhar_number bigint NOT NULL
);


ALTER TABLE public.admin_isolated OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16504)
-- Name: admin_page; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admin_page (
    mail_id character varying(50),
    username character varying(50),
    password character varying(15),
    aadhar_number integer NOT NULL,
    a_user_key integer NOT NULL
);


ALTER TABLE public.admin_page OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16653)
-- Name: cart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart (
    produ_id integer NOT NULL,
    cart_id integer NOT NULL,
    produ_name character varying(50),
    produ_price integer,
    produ_quantity integer
);


ALTER TABLE public.cart OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16640)
-- Name: order_sum; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_sum (
    o_id integer NOT NULL,
    o_cart_id integer,
    price integer,
    p_name character varying(50),
    address character varying(500),
    user_num bigint,
    user_mail_id character varying(50)
);


ALTER TABLE public.order_sum OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16519)
-- Name: product_cat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_cat (
    product_name character varying(50) NOT NULL,
    product_desc text NOT NULL,
    product_id integer NOT NULL,
    product_price integer NOT NULL,
    product_discounts integer NOT NULL,
    product_count integer NOT NULL,
    product_img character varying(500) NOT NULL,
    a_user_key integer
);


ALTER TABLE public.product_cat OWNER TO postgres;

--
-- TOC entry 4767 (class 2604 OID 16480)
-- Name: acess un_key; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acess ALTER COLUMN un_key SET DEFAULT nextval('public.acess_un_key_seq'::regclass);


--
-- TOC entry 4937 (class 0 OID 16454)
-- Dependencies: 218
-- Data for Name: acess; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.acess (username, mail_id, age, dateof_birth, password, phone_no, un_key) FROM stdin;
Ashvanthan	ashvanthansr@gmail.com	19	2006-10-16	Ashvanth	7551073112	17582824
\.


--
-- TOC entry 4941 (class 0 OID 16531)
-- Dependencies: 222
-- Data for Name: admin_isolated; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.admin_isolated (mail_id, username, password, aadhar_number) FROM stdin;
\.


--
-- TOC entry 4939 (class 0 OID 16504)
-- Dependencies: 220
-- Data for Name: admin_page; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.admin_page (mail_id, username, password, aadhar_number, a_user_key) FROM stdin;
example1@gmail.com	Example1	exmp1	7849512	3062
example2@gmail.com	Example2	exmp2	7149512	4690
example3@gmail.com	Example3	exam3	7140512	2692
example5@gmail.com	Example5	exam5	1340512	8208
example4@gmail.com	Example4	exmp4	961245	3997
\.


--
-- TOC entry 4943 (class 0 OID 16653)
-- Dependencies: 224
-- Data for Name: cart; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cart (produ_id, cart_id, produ_name, produ_price, produ_quantity) FROM stdin;
120001	17582824	HP VICTUS	90725	1
120004	17582824	HP Omen	133400	2
\.


--
-- TOC entry 4942 (class 0 OID 16640)
-- Dependencies: 223
-- Data for Name: order_sum; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_sum (o_id, o_cart_id, price, p_name, address, user_num, user_mail_id) FROM stdin;
1	17582824	175000	Lenovo Legion (x1)	Flat no 3 Door no:45 Chennai -107 Neerkundram	\N	ashvanthansr@gmail.com
2	17582824	40500	Lenovo IdeaPad (x1)	Flat no 3 Door no:45 Chennai -107 Neerkundram	\N	ashvanthansr@gmail.com
3	17582824	175000	Lenovo Legion (x1)	sd	\N	ashvanthansr@gmail.com
4	17582824	40500	Lenovo IdeaPad (x1)	sd	\N	ashvanthansr@gmail.com
5	17582824	90725	HP VICTUS (x1)	No 15 Door no 24 Neerkundram Chennai -107	\N	ashvanthansr@gmail.com
6	17582824	266800	HP Omen (x2)	No 15 Door no 24 Neerkundram Chennai -107	\N	ashvanthansr@gmail.com
\.


--
-- TOC entry 4940 (class 0 OID 16519)
-- Dependencies: 221
-- Data for Name: product_cat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_cat (product_name, product_desc, product_id, product_price, product_discounts, product_count, product_img, a_user_key) FROM stdin;
HP VICTUS	High-performance gaming laptop with AMD Ryzen 7 / Intel Core i7, NVIDIA GeForce RTX 4050, 16GB RAM, and a 144Hz FHD display for competitive gaming.	120001	90725	5	450	https://www.hp.com/wcsstore/hpusstore/Treatment/mdps/Q3FY21_omen_victus16/omen_victus_heroImage2.png	3062
Lenovo IdeaPad	Sleek, lightweight laptop perfect for everyday use and student needs. Features an AMD Ryzen 5 / Intel Core i5 processor, 8GB DDR4 RAM, and a 512GB SSD for quick boot-up	120002	40500	10	620	https://tse1.mm.bing.net/th/id/OIP.FTSxii11IDsTTFPoToVRaQHaG9?rs=1&pid=ImgDetMain&o=7&rm=3	3062
Lenovo Legion	Ultimate gaming powerhouse featuring a 16-inch QHD display, Intel Core i9, 32GB RAM, and a powerful NVIDIA RTX 4080 graphics card for peak performance and visual fidelity	120003	175000	0	250	https://m.media-amazon.com/images/I/71R2o5nKzCL._SL1500_.jpg	3062
HP Omen	Flagship gaming rig designed for enthusiasts. Features a blazing-fast 12th Gen Intel Core i7, 32GB RAM, 1TB NVMe SSD, and a vibrant 165Hz QHD display, cooled by advanced OMEN Tempest Cooling Technology	120004	133400	8	300	https://m.media-amazon.com/images/I/71Y+wQoW3CL._SL1500_.jpg	3062
HP Pavilion	A versatile and stylish everyday laptop, featuring an Intel Core i5 processor, 16GB RAM, 512GB SSD, and a long-lasting battery, ideal for creativity, streaming, and work	120005	59840	12	550	https://m.media-amazon.com/images/I/71Y+OQ3g2BL._SL1500_.jpg	3062
Iphone17	Next-generation smartphone with the A19 Bionic chip, advanced under-display Face ID, a dynamic 165Hz ProMotion display, and a groundbreaking 60MP main camera sensor	130001	128250	5	700	https://m.media-amazon.com/images/I/71Y+wQoW3CL._SL1500_.jpg	4690
Samsung S25	Future flagship powered by the Exynos 2500/Snapdragon 8 Gen 5 processor, featuring a revolutionary 200MP camera system, advanced AI processing, and a stunning 120Hz Dynamic AMOLED display.	120006	108000	10	850	https://m.media-amazon.com/images/I/71Y+wQoW3CL._SL1500_.jpg	4690
Xiaomi 17 Pro Max	Flagship Android powerhouse with a Leica co-engineered 250MP camera system, advanced Liquid Cooling technology, 120W HyperCharge, and a stunning 144Hz AMOLED curved display	120007	102300	7	600	https://m.media-amazon.com/images/I/71Y+wQoW3CL._SL1500_.jpg	4690
Oppo A5	An affordable, entry-level smartphone designed for reliability. Features a massive 5000mAh battery, a clear 6.5-inch display, and a dual-camera system, perfect for daily communication and entertainment	120008	12750	15	900	https://m.media-amazon.com/images/I/61+Q33n9tPL._SL1500_.jpg	4690
Samsung S25 Ultra	The pinnacle of mobile technology. Features an exclusive Snapdragon 8 Gen 5 for Galaxy, a titanium chassis, a 10x optical zoom periscope camera, and a built-in S Pen with ultra-low latency	120009	147250	5	400	https://m.media-amazon.com/images/I/71Y+wQoW3CL._SL1500_.jpg	4690
Logitech MX Master 5	Advanced wireless mouse designed for creative professionals. Features MagSpeed electromagnetic scrolling, Darkfield 8000 DPI tracking (works on glass), and Logi Flow for seamless control across three devices	140001	11875	5	1500	https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6290/6290218cv11d.jpg	2692
Crucial Portable SSD (1TB)	Rugged and reliable 1TB external SSD with blazing-fast USB 3.2 Gen 2 connectivity, offering read/write speeds up to 1050 MB/s. Perfect for professional backups and large file transfers	140002	7110	10	580	https://content.crucial.com/content/dam/crucial/ssd-products/x9/photography/isolated/crucial-x9-isolated-left.psd.transform/large-png/image.png	2692
TP-Link AX10	Budget-friendly Wi-Fi 6 (802.11ax) router. Delivers speeds up to 1.5 Gbps, perfect for HD streaming and gaming on multiple devices. Features 4 Gigabit LAN ports and 4 external antennas	120010	3825	15	650	https://tse3.mm.bing.net/th/id/OIP.hm_VPwurwU2c6KTVRwbh3wHaFj?rs=1&pid=ImgDetMain&o=7&rm=3	2692
Anker 313 45W USB-C Charger	Ultra-compact, high-speed 45W USB-C charger utilizing GaN technology. Perfect for fast-charging laptops, tablets, and phones. Features PowerIQ 3.0 for optimized charging and superior portability	120011	2610	10	750	https://down-sg.img.susercontent.com/file/sg-11134207-7rbkl-loxufpkwcavc0f	2692
Samsung A34	6.6″ display, Dimensity 1080 chipset, 5000 mAh battery, 256 GB storage, 8 GB RAM, Corning Gorilla Glass 5	120012	23280	3	457	https://tse2.mm.bing.net/th/id/OIP.-7FialhfHJabSvMsgWS2YAHaHa?rs=1&pid=ImgDetMain&o=7&rm=3	3062
Samsung A34	6.6″ display, Dimensity 1080 chipset, 5000 mAh battery, 256 GB storage, 8 GB RAM, Corning Gorilla Glass 5.	120013	24440	6	478	https://tse2.mm.bing.net/th/id/OIP.-7FialhfHJabSvMsgWS2YAHaHa?rs=1&pid=ImgDetMain&o=7&rm=3	3062
\.


--
-- TOC entry 4950 (class 0 OID 0)
-- Dependencies: 219
-- Name: acess_un_key_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.acess_un_key_seq', 6, true);


--
-- TOC entry 4769 (class 2606 OID 16460)
-- Name: acess acess_mail_is_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acess
    ADD CONSTRAINT acess_mail_is_key UNIQUE (mail_id);


--
-- TOC entry 4771 (class 2606 OID 16482)
-- Name: acess acess_un_key_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acess
    ADD CONSTRAINT acess_un_key_key UNIQUE (un_key);


--
-- TOC entry 4783 (class 2606 OID 16673)
-- Name: admin_isolated admin_isolated_aadhar_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_isolated
    ADD CONSTRAINT admin_isolated_aadhar_number_key UNIQUE (aadhar_number);


--
-- TOC entry 4777 (class 2606 OID 16510)
-- Name: admin_page admin_page_aadhar_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_page
    ADD CONSTRAINT admin_page_aadhar_number_key UNIQUE (aadhar_number);


--
-- TOC entry 4787 (class 2606 OID 16665)
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (produ_id, cart_id);


--
-- TOC entry 4785 (class 2606 OID 16646)
-- Name: order_sum order_sum_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_sum
    ADD CONSTRAINT order_sum_pkey PRIMARY KEY (o_id);


--
-- TOC entry 4779 (class 2606 OID 16606)
-- Name: admin_page pk_admin_page; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin_page
    ADD CONSTRAINT pk_admin_page PRIMARY KEY (a_user_key);


--
-- TOC entry 4781 (class 2606 OID 16525)
-- Name: product_cat product_cat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_cat
    ADD CONSTRAINT product_cat_pkey PRIMARY KEY (product_id);


--
-- TOC entry 4773 (class 2606 OID 16492)
-- Name: acess un_key_primary; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acess
    ADD CONSTRAINT un_key_primary PRIMARY KEY (un_key);


--
-- TOC entry 4775 (class 2606 OID 16490)
-- Name: acess username_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.acess
    ADD CONSTRAINT username_unique UNIQUE (username);


--
-- TOC entry 4789 (class 2620 OID 16494)
-- Name: acess before_insert_acess; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER before_insert_acess BEFORE INSERT ON public.acess FOR EACH ROW EXECUTE FUNCTION public.generate_un_key();


--
-- TOC entry 4790 (class 2620 OID 16613)
-- Name: admin_page before_insert_adminpage; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER before_insert_adminpage BEFORE INSERT ON public.admin_page FOR EACH ROW EXECUTE FUNCTION public.assign_user_key();


--
-- TOC entry 4791 (class 2620 OID 16528)
-- Name: product_cat trg_product_id; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_product_id BEFORE INSERT ON public.product_cat FOR EACH ROW EXECUTE FUNCTION public.generate_product_id();


--
-- TOC entry 4788 (class 2606 OID 16658)
-- Name: cart cart_produ_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_produ_id_fkey FOREIGN KEY (produ_id) REFERENCES public.product_cat(product_id);


-- Completed on 2025-10-26 14:02:47

--
-- PostgreSQL database dump complete
--

\unrestrict nkb59Fh1Vgm37viJdv0qVU498ORFDccNvmQPyv5PtpzyNqXScNBSUtcb8igMe7t

