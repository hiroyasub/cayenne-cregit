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
name|tools
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|gen
operator|.
name|DefaultClassGenerator
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
name|map
operator|.
name|ObjEntity
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
name|util
operator|.
name|Util
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

begin_comment
comment|/**  * Utility class to perform class generation from data map. This class is used by  * ant and Maven plugins.  *   * @author Andrus Adamchik, Kevin Menard  * @since 3.0  */
end_comment

begin_class
class|class
name|CayenneGeneratorUtil
block|{
specifier|protected
name|ILog
name|logger
decl_stmt|;
specifier|protected
name|File
name|map
decl_stmt|;
specifier|protected
name|File
name|additionalMaps
index|[]
decl_stmt|;
specifier|protected
name|DefaultClassGenerator
name|generator
decl_stmt|;
specifier|protected
name|String
name|includeEntitiesPattern
decl_stmt|;
specifier|protected
name|String
name|excludeEntitiesPattern
decl_stmt|;
comment|/** Loads and returns a DataMap by File. */
specifier|public
name|DataMap
name|loadDataMap
parameter_list|(
name|File
name|mapName
parameter_list|)
throws|throws
name|Exception
block|{
name|InputSource
name|in
init|=
operator|new
name|InputSource
argument_list|(
name|mapName
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|MapLoader
argument_list|()
operator|.
name|loadDataMap
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/** Loads and returns DataMap based on<code>map</code> attribute. */
specifier|public
name|DataMap
name|loadDataMap
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|loadDataMap
argument_list|(
name|map
argument_list|)
return|;
block|}
comment|/** Loads and returns DataMap based on<code>map</code> attribute. */
specifier|protected
name|DataMap
index|[]
name|loadAdditionalDataMaps
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
literal|null
operator|==
name|additionalMaps
condition|)
block|{
return|return
operator|new
name|DataMap
index|[
literal|0
index|]
return|;
block|}
name|DataMap
name|dataMaps
index|[]
init|=
operator|new
name|DataMap
index|[
name|additionalMaps
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|additionalMaps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|dataMaps
index|[
name|i
index|]
operator|=
name|loadDataMap
argument_list|(
name|additionalMaps
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|dataMaps
return|;
block|}
specifier|public
name|void
name|processMap
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|dataMap
init|=
name|loadDataMap
argument_list|()
decl_stmt|;
name|DataMap
name|additionalDataMaps
index|[]
init|=
name|loadAdditionalDataMaps
argument_list|()
decl_stmt|;
comment|// Create MappingNamespace for maps.
name|EntityResolver
name|entityResolver
init|=
operator|new
name|EntityResolver
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|dataMap
argument_list|)
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|setNamespace
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|additionalDataMaps
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|entityResolver
operator|.
name|addDataMap
argument_list|(
name|additionalDataMaps
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|additionalDataMaps
index|[
name|i
index|]
operator|.
name|setNamespace
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
block|}
name|Collection
name|allEntities
init|=
name|dataMap
operator|.
name|getObjEntities
argument_list|()
decl_stmt|;
name|List
name|filteredEntities
init|=
operator|new
name|ArrayList
argument_list|(
name|allEntities
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
comment|// filter client entities
if|if
condition|(
name|generator
operator|.
name|isClient
argument_list|()
condition|)
block|{
if|if
condition|(
name|dataMap
operator|.
name|isClientSupported
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|allEntities
operator|.
name|iterator
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
name|ObjEntity
name|entity
init|=
operator|(
name|ObjEntity
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|isClientAllowed
argument_list|()
condition|)
block|{
name|filteredEntities
operator|.
name|add
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|filteredEntities
operator|.
name|addAll
argument_list|(
name|allEntities
argument_list|)
expr_stmt|;
block|}
comment|// filter names according to the specified pattern
name|NamePatternMatcher
name|namePatternMatcher
init|=
operator|new
name|NamePatternMatcher
argument_list|(
name|logger
argument_list|,
name|includeEntitiesPattern
argument_list|,
name|excludeEntitiesPattern
argument_list|)
decl_stmt|;
name|namePatternMatcher
operator|.
name|filter
argument_list|(
name|filteredEntities
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setTimestamp
argument_list|(
name|map
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setObjEntities
argument_list|(
name|filteredEntities
argument_list|)
expr_stmt|;
name|generator
operator|.
name|validateAttributes
argument_list|()
expr_stmt|;
name|generator
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|processMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|th
operator|=
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
expr_stmt|;
name|String
name|thMessage
init|=
name|th
operator|.
name|getLocalizedMessage
argument_list|()
decl_stmt|;
name|String
name|message
init|=
literal|"Error generating classes: "
decl_stmt|;
name|message
operator|+=
operator|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|thMessage
argument_list|)
operator|)
condition|?
name|thMessage
else|:
name|th
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|logger
operator|.
name|log
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setAdditionalMaps
parameter_list|(
name|File
index|[]
name|additionalMaps
parameter_list|)
block|{
name|this
operator|.
name|additionalMaps
operator|=
name|additionalMaps
expr_stmt|;
block|}
specifier|public
name|void
name|setExcludeEntitiesPattern
parameter_list|(
name|String
name|excludeEntitiesPattern
parameter_list|)
block|{
name|this
operator|.
name|excludeEntitiesPattern
operator|=
name|excludeEntitiesPattern
expr_stmt|;
block|}
specifier|public
name|void
name|setGenerator
parameter_list|(
name|DefaultClassGenerator
name|generator
parameter_list|)
block|{
name|this
operator|.
name|generator
operator|=
name|generator
expr_stmt|;
block|}
specifier|public
name|void
name|setIncludeEntitiesPattern
parameter_list|(
name|String
name|includeEntitiesPattern
parameter_list|)
block|{
name|this
operator|.
name|includeEntitiesPattern
operator|=
name|includeEntitiesPattern
expr_stmt|;
block|}
specifier|public
name|void
name|setLogger
parameter_list|(
name|ILog
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
specifier|public
name|void
name|setMap
parameter_list|(
name|File
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
block|}
end_class

end_unit

