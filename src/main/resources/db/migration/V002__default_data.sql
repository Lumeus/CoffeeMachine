
COPY recipe (id, name) FROM stdin;
1	Эспрессо
2	Капучино
3	Американо
\.


COPY action (id, count, type, recipe_id) FROM stdin;
1	1	COFFEE	1
2	1	COFFEE	2
3	2	MILK	2
4	3	FOAM	2
5	1	COFFEE	3
6	2	WATER	3
\.


COPY ingredient_amount (id, amount, ingredient, action_id) FROM stdin;
1	7	COFFEE	1
2	30	WATER	1
3	14	COFFEE	2
4	60	WATER	2
5	60	MILK	3
6	60	MILK	4
7	14	COFFEE	5
8	60	WATER	5
9	140	WATER	6
\.


COPY remains (ingredient, remains, reserved) FROM stdin;
MILK	1000	0
WATER	1000	0
COFFEE	1000	0
\.

SELECT pg_catalog.setval('public.recipe_id_seq', 3, true);

SELECT pg_catalog.setval('public.action_id_seq', 6, true);

SELECT pg_catalog.setval('public.ingredient_amount_id_seq', 9, true);
