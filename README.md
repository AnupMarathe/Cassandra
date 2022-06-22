# Cassandra
## Installation on Windows
- [Apache Cassandra 3.11](https://www.apache.org/dyn/closer.lua/cassandra/3.11.13/apache-cassandra-3.11.13-bin.tar.gz). This distribution has .bat files running cassndra DB on Windows
- [Python version 2.7](https://www.python.org/downloads/release/python-2718/). The Cassandra 3.11 cqlsh works with Python 2.7
- Java 8

Install Java 8 and Python 2.7. Add system environment variable JAVA_HOME as base directory of Java installation. Add %JAVA_HOME%/bin in environment variable PATH. Similarly add Python base directory in PATH.

For local installation the port is defined in cassandra.yaml. Following is default value.
native_transport_port: 9042

## Commands

### Handling Keyspace
Following commands will create keyspace 'learning'. Then we will list all available keyspaces. To get more details about newly created keyspace, we will describe specific keyspace. After that we will use keyspace and then drop it. 

```
cqlsh> CREATE KEYSPACE learning WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : '1'};

cqlsh> DESCRIBE keyspaces

cqlsh> DESCRIBE keyspace learning

cqlsh> USE learning;
cqlsh:learning>

cqlsh:learning> DROP KEYSPACE learning;
```

### Handling Tables
First we will create a table emp with six columns. The we will check select command to get data from table.

```
cqlsh:learning> CREATE TABLE emp(
emp_id int PRIMARY KEY,
emp_name text,
emp_city text,
emp_sal varint,
emp_bonus varint,
emp_phone varint
);

cqlsh:learning> select * from emp;

 emp_id | emp_bonus | emp_city | emp_name | emp_phone | emp_sal
--------+-----------+----------+----------+-----------+---------

(0 rows)
```

It is possible to change structure of table. We can ADD or DROP columns from table. Cassandra will not give any warning or error if column with data is dropped. It will simplete delete that column. 

```
cqlsh:learning> ALTER TABLE emp
ADD emp_email text;

cqlsh:learning> select * from emp;

 emp_id | emp_bonus | emp_city | emp_email | emp_name | emp_phone  | emp_sal
--------+-----------+----------+-----------+----------+------------+---------
      6 |      null |   Nagpur |      null |    Scott | 9087654321 |   30000
      9 |         0 |     Pune |      null |     Anup | 9876543210 |   30000

(2 rows)

cqlsh:learning> ALTER TABLE emp
DROP emp_bonus;

cqlsh:learning> select * from emp;

 emp_id | emp_city | emp_email | emp_name | emp_phone  | emp_sal
--------+----------+-----------+----------+------------+---------
      6 |   Nagpur |      null |    Scott | 9087654321 |   30000
      9 |     Pune |      null |     Anup | 9876543210 |   30000

(2 rows)
```
Tables is are listed using either describing tables or columnfamilies. You can drop table with DROP table command. The table is removed from Keyspace. 

```
cqlsh:learning> describe columnfamilies

emp

cqlsh:learning> drop table emp;

cqlsh:learning> describe columnfamilies

<empty>
```
### Handling Data 
The select query returns rows from table. If we want to query using specific column, then we need to create an index on that column. Without index, the query will not work. We can force query to return data, but it can have degraded performance.

```
cqlsh:learning> select * from emp;

 emp_id | emp_bonus | emp_city | emp_email | emp_name | emp_phone  | emp_sal
--------+-----------+----------+-----------+----------+------------+---------
      6 |      null |   Nagpur |      null |    Scott | 9087654321 |   30000
      9 |         0 |     Pune |      null |     Anup | 9876543210 |   30000

(2 rows)

cqlsh:learning> select * from emp where emp_name = 'Anup';
InvalidRequest: Error from server: code=2200 [Invalid query] message="Cannot execute this query as it might involve data filtering and thus may have unpredictable performance. If you want to execute this query despite the performance unpredictability, use ALLOW FILTERING"

cqlsh:learning> select * from emp where emp_city = 'Pune' ALLOW FILTERING;

 emp_id | emp_city | emp_email | emp_name | emp_phone  | emp_sal
--------+----------+-----------+----------+------------+---------
      9 |     Pune |      null |     Anup | 9876543210 |   30000

(1 rows)

cqlsh:learning> CREATE INDEX name ON emp (emp_name);
cqlsh:learning> describe table emp;

CREATE TABLE learning.emp (
    emp_id int PRIMARY KEY,
    emp_bonus varint,
    emp_city text,
    emp_name text,
    emp_phone varint,
    emp_sal varint
) WITH bloom_filter_fp_chance = 0.01
    AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
    AND comment = ''
    AND compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
    AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
    AND crc_check_chance = 1.0
    AND dclocal_read_repair_chance = 0.1
    AND default_time_to_live = 0
    AND gc_grace_seconds = 864000
    AND max_index_interval = 2048
    AND memtable_flush_period_in_ms = 0
    AND min_index_interval = 128
    AND read_repair_chance = 0.0
    AND speculative_retry = '99PERCENTILE';
CREATE INDEX name ON learning.emp (emp_name);

cqlsh:learning> select * from emp where emp_name = 'Anup';

 emp_id | emp_bonus | emp_city | emp_name | emp_phone  | emp_sal
--------+-----------+----------+----------+------------+---------
      9 |         0 |     Pune |     Anup | 9876543210 |   30000

(1 rows)
cqlsh:learning>
```
While deleting data, we have to provide a primary key. Even when index is created for specific column, it will not allow deletion using that column. Also if we give incorrect primary key, we will not get any error.
```
cqlsh:learning> select * from emp;

 emp_id | emp_bonus | emp_city | emp_name | emp_phone  | emp_sal
--------+-----------+----------+----------+------------+---------
      7 |       560 |     null |    Scott | 9876543210 |   30000
      9 |         0 |     Pune |     Anup | 9876543210 |   30000

(2 rows)
cqlsh:learning> delete from emp where emp_name = 'Anup';
InvalidRequest: Error from server: code=2200 [Invalid query] message="Some partition key parts are missing: emp_id"

cqlsh:learning> DELETE from emp where emp_id = 6;
cqlsh:learning> select * from emp;

 emp_id | emp_bonus | emp_city | emp_name | emp_phone  | emp_sal
--------+-----------+----------+----------+------------+---------
      7 |       560 |     null |    Scott | 9876543210 |   30000
      9 |         0 |     Pune |     Anup | 9876543210 |   30000

(2 rows)
                                  ^
cqlsh:learning> DELETE from emp where emp_id = 7;
cqlsh:learning> select * from emp;

 emp_id | emp_bonus | emp_city | emp_name | emp_phone  | emp_sal
--------+-----------+----------+----------+------------+---------
      9 |         0 |     Pune |     Anup | 9876543210 |   30000

(1 rows)
cqlsh:learning>
```
