-- ==========================================
-- PARTE 1: TABELA employees
-- ==========================================

-- Este comando remove a tabela employees caso ela já exista no banco de dados, permitindo a reexecução do script sem erros.
DROP TABLE IF EXISTS employees;

-- Este comando cria a estrutura da tabela employees, definindo os tipos de dados e aplicando a restrição NOT NULL para garantir a integridade dos registros.
CREATE TABLE employees (
    emp_id SERIAL PRIMARY KEY,
    emp_name VARCHAR(100) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    salary NUMERIC(10, 2) NOT NULL,
    hire_date DATE NOT NULL
);

-- Este comando insere os registros iniciais na tabela employees, deixando o banco gerar automaticamente os IDs através do tipo SERIAL.
INSERT INTO employees (emp_name, job_title, salary, hire_date) VALUES
('Alice', 'Manager', 60000, '2020-01-15'),
('Bob', 'Developer', 50000, '2019-05-20'),
('Charlie', 'Sales', 45000, '2021-03-10'),
('Diana', 'Manager', 62000, '2022-02-28'),
('Eve', 'Marketing', 48000, '2020-11-12');


-- Este bloco contém as consultas solicitadas para a tabela employees.

-- 1. Seleciona todos os funcionários.
-- Esta consulta recupera e exibe todas as linhas e colunas existentes na tabela employees.
SELECT * FROM employees;

-- 2. Seleciona os funcionários cujo salário é superior a 55000.
-- Esta consulta filtra os dados, retornando exclusivamente os registros onde o valor do salário é estritamente maior que 55000.
SELECT * FROM employees
WHERE salary > 55000;

-- 3. Seleciona os funcionários que foram contratados depois de 2020.
-- Esta consulta seleciona os funcionários avaliando se a data de contratação é igual ou posterior a 1 de janeiro de 2021.
-- Uma abordagem alternativa seria utilizar a cláusula WHERE com EXTRACT(YEAR FROM hire_date) > 2020.
SELECT * FROM employees
WHERE hire_date >= '2021-01-01';

-- 4. Seleciona os funcionários cujos nomes começam com 'A'.
-- Esta consulta utiliza o operador LIKE para buscar registros onde o nome do funcionário inicie com a letra 'A' maiúscula, respeitando a sensibilidade a maiúsculas e minúsculas do PostgreSQL.
SELECT * FROM employees
WHERE emp_name LIKE 'A%';


-- ==========================================
-- PARTE 2: TABELAS customers E orders
-- ==========================================

-- Este comando remove as tabelas orders e customers (na ordem correta de dependência) caso elas já existam.
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

-- Este comando cria a estrutura da tabela customers.
CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE
);

-- Insere registros iniciais na tabela customers.
INSERT INTO customers (customer_name, email) VALUES
('Cliente 101', 'cliente101@email.com'),
('Cliente 102', 'cliente102@email.com'),
('Cliente 103', 'cliente103@email.com');

-- Este comando cria a estrutura da tabela orders com os respectivos tipos de dados, restrições NOT NULL e uma chave estrangeira vinculada a tabela customers.
CREATE TABLE orders (
    order_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    total_amount NUMERIC(10, 2) NOT NULL,
    order_date DATE NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Este comando insere os registros iniciais na tabela orders. Como os IDs dos clientes agora são gerados sequencialmente (1, 2, 3), vamos vincular os pedidos a esses IDs.
INSERT INTO orders (customer_id, total_amount, order_date) VALUES
(1, 250, '2023-01-05'),
(2, 300, '2023-02-10'),
(1, 150, '2023-01-20'),
(3, 200, '2023-03-15'),
(2, 350, '2023-02-28');


-- Este bloco contém as consultas solicitadas para a tabela orders.

-- 1. Calcula o total de pedidos.
-- Esta consulta utiliza a função de agregação COUNT para calcular e retornar a quantidade total de registros (pedidos) presentes na tabela.
SELECT COUNT(*) AS total_de_pedidos
FROM orders;

-- 2. Calcula o total de pedidos para cada cliente.
-- Esta consulta agrupa os dados com base no identificador do cliente (customer_id). Para cada grupo, ela calcula a quantidade total de pedidos e a soma dos valores gastos.
SELECT
    customer_id,
    COUNT(*) AS quantidade_pedidos,
    SUM(total_amount) AS valor_total_gasto
FROM orders
GROUP BY customer_id
ORDER BY customer_id;

-- 3. Lista a média e o valor total de pedidos.
-- Esta consulta calcula a média aritmética de todos os pedidos, arredondando o resultado para duas casas decimais, e também soma o valor total de todos os registros.
SELECT
    ROUND(AVG(total_amount), 2) AS media_dos_pedidos,
    SUM(total_amount) AS valor_total_dos_pedidos
FROM orders;