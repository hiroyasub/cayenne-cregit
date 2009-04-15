begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|dbsync
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|MockOperationObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|dbsync
operator|.
name|CreateIfNoSchemaStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|dbsync
operator|.
name|SchemaUpdateStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|dbsync
operator|.
name|ThrowOnPartialOrCreateSchemaStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|dbsync
operator|.
name|ThrowOnPartialSchemaStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|JdbcAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntityResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|MapLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SQLTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_class
specifier|public
class|class
name|SchemaUpdateStrategyTest
extends|extends
name|CayenneCase
block|{
specifier|private
name|DataMap
name|dataMap
decl_stmt|;
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
if|if
condition|(
name|dataMap
operator|==
literal|null
condition|)
block|{
name|MapLoader
name|mapLoader
init|=
operator|new
name|MapLoader
argument_list|()
decl_stmt|;
name|dataMap
operator|=
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
name|getMapXml
argument_list|(
literal|"sus-map.map.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|dataMap
return|;
block|}
specifier|private
name|InputSource
name|getMapXml
parameter_list|(
name|String
name|mapName
parameter_list|)
block|{
return|return
operator|new
name|InputSource
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|mapName
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|testDBGeneratorStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|template
init|=
literal|"SELECT #result('id' 'int') FROM SUS1"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
name|getDataMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|DataNode
name|dataNode
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|int
name|sizeDB
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
try|try
block|{
name|generateDBWithDBGeneratorStrategy
argument_list|(
name|dataNode
argument_list|,
name|query
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|int
name|sizeDB2
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sizeDB2
operator|-
name|sizeDB
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|int
name|sizeDB3
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|sizeDB2
argument_list|,
name|sizeDB3
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|DataNode
name|dataNode2
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|dataNode2
operator|.
name|setSchemaUpdateStrategy
argument_list|(
operator|(
name|SchemaUpdateStrategy
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|dataNode2
operator|.
name|getSchemaUpdateStrategyName
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
name|dropTables
argument_list|(
name|map
argument_list|,
name|dataNode2
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|sizeDB
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testThrowOnPartialStrategyTableNoExist
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
name|getDataMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|DataNode
name|dataNode
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|setStrategy
argument_list|(
name|ThrowOnPartialSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
try|try
block|{
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testThrowOnPartialStrategyTableExist
parameter_list|()
throws|throws
name|Exception
block|{
name|tableExistfForThrowOnPartialAndMixStrategy
argument_list|(
name|ThrowOnPartialSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testThrowOnPartialStrategyWithOneTable
parameter_list|()
throws|throws
name|Exception
block|{
name|withOneTableForThrowOnPartialAndMixStrategy
argument_list|(
name|ThrowOnPartialSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMixedStrategyTableNoExist
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|template
init|=
literal|"SELECT #result('id' 'int') FROM SUS1"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
name|getDataMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|DataNode
name|dataNode
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|int
name|sizeDB
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|setStrategy
argument_list|(
name|ThrowOnPartialOrCreateSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
try|try
block|{
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|nameTables
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nameTables
operator|.
name|get
argument_list|(
literal|"SUS1"
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|sizeDB2
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sizeDB2
operator|-
name|sizeDB
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|int
name|sizeDB3
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|sizeDB2
argument_list|,
name|sizeDB3
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|DataNode
name|dataNode2
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|dataNode2
operator|.
name|setSchemaUpdateStrategy
argument_list|(
operator|(
name|SchemaUpdateStrategy
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|dataNode2
operator|.
name|getSchemaUpdateStrategyName
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
name|dropTables
argument_list|(
name|map
argument_list|,
name|dataNode2
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|sizeDB
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMixedStrategyTableExist
parameter_list|()
throws|throws
name|Exception
block|{
name|tableExistfForThrowOnPartialAndMixStrategy
argument_list|(
name|ThrowOnPartialOrCreateSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMixedStrategyWithOneTable
parameter_list|()
throws|throws
name|Exception
block|{
name|withOneTableForThrowOnPartialAndMixStrategy
argument_list|(
name|ThrowOnPartialOrCreateSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
specifier|public
name|void
name|testNoStandartSchema
parameter_list|()
block|{
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
name|getDataMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|DataNode
name|dataNode
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|setStrategy
argument_list|(
name|TestSchemaUpdateStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataNode
operator|.
name|getSchemaUpdateStrategy
argument_list|()
operator|instanceof
name|TestSchemaUpdateStrategy
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|withOneTableForThrowOnPartialAndMixStrategy
parameter_list|(
name|String
name|strategy
parameter_list|)
block|{
name|DbEntity
name|entity
init|=
literal|null
decl_stmt|;
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
name|getDataMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|DataNode
name|dataNode
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|DataNode
name|dataNode2
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|sizeDB
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|entity
operator|=
name|createOneTable
argument_list|(
name|dataNode
argument_list|)
expr_stmt|;
name|int
name|sizeDB2
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sizeDB2
operator|-
name|sizeDB
argument_list|)
expr_stmt|;
name|setStrategy
argument_list|(
name|strategy
argument_list|,
name|dataNode2
argument_list|)
expr_stmt|;
name|dataNode2
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|dataNode2
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|template2
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|dropTableStatements
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|template2
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Query
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Query
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|SQLTemplate
name|q
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
name|dataNode
operator|.
name|performQueries
argument_list|(
name|list
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|tableExistfForThrowOnPartialAndMixStrategy
parameter_list|(
name|String
name|strategy
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|template
init|=
literal|"SELECT #result('ARTIST_ID' 'int') FROM ARTIST ORDER BY ARTIST_ID"
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
name|getDataMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|DataNode
name|dataNode
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|int
name|sizeDB
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|generateDBWithDBGeneratorStrategy
argument_list|(
name|dataNode
argument_list|,
name|query
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|int
name|sizeDB2
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sizeDB2
operator|-
name|sizeDB
argument_list|)
expr_stmt|;
try|try
block|{
name|DataNode
name|dataNode2
init|=
name|createDataNode
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|setStrategy
argument_list|(
name|strategy
argument_list|,
name|dataNode2
argument_list|)
expr_stmt|;
name|dataNode2
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|dataNode2
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|dropTables
argument_list|(
name|map
argument_list|,
name|dataNode
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|DbEntity
name|createOneTable
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|getDataMap
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|ent
init|=
name|map
operator|.
name|getDbEntities
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
name|ent
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|template
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|createTable
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|setStrategy
argument_list|(
literal|null
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
return|return
name|entity
return|;
block|}
specifier|private
name|DataNode
name|createDataNode
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|JdbcAdapter
name|adapter
init|=
operator|(
name|JdbcAdapter
operator|)
name|getAccessStackAdapter
argument_list|()
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|colection
init|=
operator|new
name|ArrayList
argument_list|<
name|DataMap
argument_list|>
argument_list|()
decl_stmt|;
name|colection
operator|.
name|add
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|DataNode
name|dataNode
init|=
operator|new
name|DataNode
argument_list|()
decl_stmt|;
name|dataNode
operator|.
name|setDataMaps
argument_list|(
name|colection
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setDataSource
argument_list|(
name|getNode
argument_list|()
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setDataSourceFactory
argument_list|(
name|getNode
argument_list|()
operator|.
name|getDataSourceFactory
argument_list|()
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setSchemaUpdateStrategyName
argument_list|(
name|getNode
argument_list|()
operator|.
name|getSchemaUpdateStrategyName
argument_list|()
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|EntityResolver
argument_list|(
name|colection
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dataNode
return|;
block|}
specifier|private
name|void
name|generateDBWithDBGeneratorStrategy
parameter_list|(
name|DataNode
name|dataNode
parameter_list|,
name|SQLTemplate
name|query
parameter_list|,
name|MockOperationObserver
name|observer
parameter_list|)
block|{
name|setStrategy
argument_list|(
name|CreateIfNoSchemaStrategy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|query
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|nameTables
init|=
name|getNameTablesInDB
argument_list|(
name|dataNode
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|nameTables
operator|.
name|get
argument_list|(
literal|"SUS1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setStrategy
parameter_list|(
name|String
name|name
parameter_list|,
name|DataNode
name|dataNode
parameter_list|)
block|{
name|dataNode
operator|.
name|setSchemaUpdateStrategyName
argument_list|(
name|name
argument_list|)
expr_stmt|;
try|try
block|{
name|dataNode
operator|.
name|setSchemaUpdateStrategy
argument_list|(
operator|(
name|SchemaUpdateStrategy
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|dataNode
operator|.
name|getSchemaUpdateStrategyName
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dropTables
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|DataNode
name|dataNode
parameter_list|,
name|MockOperationObserver
name|observer
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|ent
init|=
name|map
operator|.
name|getDbEntities
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|DbEntity
argument_list|>
name|iterator
init|=
name|ent
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|collectionDrop
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|dropTableStatements
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|collectionDrop
control|)
block|{
name|SQLTemplate
name|queryDrop
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|s
argument_list|)
decl_stmt|;
name|dataNode
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|Query
operator|)
name|queryDrop
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|getNameTablesInDB
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
block|{
name|String
name|tableLabel
init|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
name|Connection
name|con
init|=
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|nameTables
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|con
operator|=
name|dataNode
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|ResultSet
name|rs
init|=
name|con
operator|.
name|getMetaData
argument_list|()
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"%"
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|)
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
name|nameTables
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|nameTables
return|;
block|}
block|}
end_class

end_unit

