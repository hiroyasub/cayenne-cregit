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
name|dbsync
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|configuration
operator|.
name|xml
operator|.
name|DataMapLoaderListener
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
name|configuration
operator|.
name|xml
operator|.
name|NamespaceAwareNestedTagHandler
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeColumn
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeProcedure
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeProcedure
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeTable
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeColumn
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
name|xml
operator|.
name|sax
operator|.
name|Attributes
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
name|ContentHandler
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
name|SAXException
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
class|class
name|ConfigHandler
extends|extends
name|NamespaceAwareNestedTagHandler
block|{
specifier|static
specifier|final
name|String
name|OLD_CONFIG_TAG
init|=
literal|"config"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CONFIG_TAG
init|=
literal|"dbImport"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CATALOG_TAG
init|=
literal|"catalog"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SCHEMA_TAG
init|=
literal|"schema"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TABLE_TYPE_TAG
init|=
literal|"tableType"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_PACKAGE_TAG
init|=
literal|"defaultPackage"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FORCE_DATAMAP_CATALOG_TAG
init|=
literal|"forceDataMapCatalog"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FORCE_DATAMAP_SCHEMA_TAG
init|=
literal|"forceDataMapSchema"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MEANINGFUL_PK_TABLES_TAG
init|=
literal|"meaningfulPkTables"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAMING_STRATEGY_TAG
init|=
literal|"namingStrategy"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SKIP_PK_LOADING_TAG
init|=
literal|"skipPrimaryKeyLoading"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SKIP_RELATIONSHIPS_LOADING_TAG
init|=
literal|"skipRelationshipsLoading"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STRIP_FROM_TABLE_NAMES_TAG
init|=
literal|"stripFromTableNames"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USE_JAVA7_TYPES_TAG
init|=
literal|"useJava7Types"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USE_PRIMITIVES_TAG
init|=
literal|"usePrimitives"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INCLUDE_TABLE_TAG
init|=
literal|"includeTable"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUDE_TABLE_TAG
init|=
literal|"excludeTable"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INCLUDE_COLUMN_TAG
init|=
literal|"includeColumn"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUDE_COLUMN_TAG
init|=
literal|"excludeColumn"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INCLUDE_PROCEDURE_TAG
init|=
literal|"includeProcedure"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXCLUDE_PROCEDURE_TAG
init|=
literal|"excludeProcedure"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TRUE
init|=
literal|"true"
decl_stmt|;
specifier|private
name|ReverseEngineering
name|configuration
decl_stmt|;
specifier|private
name|DataChannelMetaData
name|metaData
decl_stmt|;
name|ConfigHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parentHandler
parameter_list|,
name|DataChannelMetaData
name|metaData
parameter_list|)
block|{
name|super
argument_list|(
name|parentHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|metaData
operator|=
name|metaData
expr_stmt|;
name|this
operator|.
name|targetNamespace
operator|=
name|DbImportExtension
operator|.
name|NAMESPACE
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|processElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|CONFIG_TAG
case|:
name|createConfig
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
case|case
name|OLD_CONFIG_TAG
case|:
name|createConfig
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ContentHandler
name|createChildTagHandler
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
block|{
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|targetNamespace
argument_list|)
condition|)
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|CATALOG_TAG
case|:
return|return
operator|new
name|CatalogHandler
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
case|case
name|SCHEMA_TAG
case|:
return|return
operator|new
name|SchemaHandler
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
case|case
name|INCLUDE_TABLE_TAG
case|:
return|return
operator|new
name|IncludeTableHandler
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
return|return
name|super
operator|.
name|createChildTagHandler
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|,
name|attributes
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processCharData
parameter_list|(
name|String
name|localName
parameter_list|,
name|String
name|data
parameter_list|)
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|TABLE_TYPE_TAG
case|:
name|createTableType
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|DEFAULT_PACKAGE_TAG
case|:
name|createDefaultPackage
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|FORCE_DATAMAP_CATALOG_TAG
case|:
name|createForceDatamapCatalog
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|FORCE_DATAMAP_SCHEMA_TAG
case|:
name|createForceDatamapSchema
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|MEANINGFUL_PK_TABLES_TAG
case|:
name|createMeaningfulPkTables
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|NAMING_STRATEGY_TAG
case|:
name|createNamingStrategy
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|SKIP_PK_LOADING_TAG
case|:
name|createSkipPkLoading
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|SKIP_RELATIONSHIPS_LOADING_TAG
case|:
name|createSkipRelationshipsLoading
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|STRIP_FROM_TABLE_NAMES_TAG
case|:
name|createStripFromTableNames
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|USE_JAVA7_TYPES_TAG
case|:
name|createUseJava7Types
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|USE_PRIMITIVES_TAG
case|:
name|createUsePrimitives
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXCLUDE_TABLE_TAG
case|:
name|createExcludeTable
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|INCLUDE_COLUMN_TAG
case|:
name|createIncludeColumn
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXCLUDE_COLUMN_TAG
case|:
name|createExcludeColumn
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|INCLUDE_PROCEDURE_TAG
case|:
name|createIncludeProcedure
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXCLUDE_PROCEDURE_TAG
case|:
name|createExcludeProcedure
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
specifier|private
name|void
name|createExcludeProcedure
parameter_list|(
name|String
name|excludeProcedure
parameter_list|)
block|{
if|if
condition|(
name|excludeProcedure
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|addExcludeProcedure
argument_list|(
operator|new
name|ExcludeProcedure
argument_list|(
name|excludeProcedure
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createIncludeProcedure
parameter_list|(
name|String
name|includeProcedure
parameter_list|)
block|{
if|if
condition|(
name|includeProcedure
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|addIncludeProcedure
argument_list|(
operator|new
name|IncludeProcedure
argument_list|(
name|includeProcedure
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createExcludeColumn
parameter_list|(
name|String
name|excludeColumn
parameter_list|)
block|{
if|if
condition|(
name|excludeColumn
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
name|excludeColumn
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createIncludeColumn
parameter_list|(
name|String
name|includeColumn
parameter_list|)
block|{
if|if
condition|(
name|includeColumn
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
name|includeColumn
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createExcludeTable
parameter_list|(
name|String
name|excludeTable
parameter_list|)
block|{
if|if
condition|(
name|excludeTable
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
name|excludeTable
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createUsePrimitives
parameter_list|(
name|String
name|usePrimitives
parameter_list|)
block|{
if|if
condition|(
name|usePrimitives
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|usePrimitives
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setUsePrimitives
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setUsePrimitives
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createUseJava7Types
parameter_list|(
name|String
name|useJava7Types
parameter_list|)
block|{
if|if
condition|(
name|useJava7Types
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|useJava7Types
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setUseJava7Types
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setUseJava7Types
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createStripFromTableNames
parameter_list|(
name|String
name|stripFromTableNames
parameter_list|)
block|{
if|if
condition|(
name|stripFromTableNames
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setStripFromTableNames
argument_list|(
name|stripFromTableNames
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createSkipRelationshipsLoading
parameter_list|(
name|String
name|skipRelationshipsLoading
parameter_list|)
block|{
if|if
condition|(
name|skipRelationshipsLoading
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|skipRelationshipsLoading
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setSkipRelationshipsLoading
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setSkipRelationshipsLoading
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createSkipPkLoading
parameter_list|(
name|String
name|skipPkLoading
parameter_list|)
block|{
if|if
condition|(
name|skipPkLoading
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|skipPkLoading
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createNamingStrategy
parameter_list|(
name|String
name|namingStrategy
parameter_list|)
block|{
if|if
condition|(
name|namingStrategy
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setNamingStrategy
argument_list|(
name|namingStrategy
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createMeaningfulPkTables
parameter_list|(
name|String
name|meaningfulPkTables
parameter_list|)
block|{
if|if
condition|(
name|meaningfulPkTables
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setMeaningfulPkTables
argument_list|(
name|meaningfulPkTables
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createForceDatamapSchema
parameter_list|(
name|String
name|forceDatamapSchema
parameter_list|)
block|{
if|if
condition|(
name|forceDatamapSchema
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|forceDatamapSchema
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setForceDataMapSchema
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setForceDataMapSchema
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createForceDatamapCatalog
parameter_list|(
name|String
name|forceDatamapCatalog
parameter_list|)
block|{
if|if
condition|(
name|forceDatamapCatalog
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|forceDatamapCatalog
operator|.
name|equals
argument_list|(
name|TRUE
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setForceDataMapCatalog
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setForceDataMapCatalog
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|createDefaultPackage
parameter_list|(
name|String
name|defaultPackage
parameter_list|)
block|{
if|if
condition|(
name|defaultPackage
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setDefaultPackage
argument_list|(
name|defaultPackage
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createTableType
parameter_list|(
name|String
name|tableType
parameter_list|)
block|{
if|if
condition|(
name|tableType
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|addTableType
argument_list|(
name|tableType
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createConfig
parameter_list|()
block|{
name|configuration
operator|=
operator|new
name|ReverseEngineering
argument_list|()
expr_stmt|;
name|loaderContext
operator|.
name|addDataMapListener
argument_list|(
operator|new
name|DataMapLoaderListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDataMapLoaded
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|ConfigHandler
operator|.
name|this
operator|.
name|metaData
operator|.
name|add
argument_list|(
name|dataMap
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

