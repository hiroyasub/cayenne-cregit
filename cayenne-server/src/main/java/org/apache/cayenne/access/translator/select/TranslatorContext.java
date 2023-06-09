begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|translator
operator|.
name|select
package|;
end_package

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
name|LinkedList
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
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|sqlbuilder
operator|.
name|SQLGenerationContext
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
name|sqlbuilder
operator|.
name|SelectBuilder
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
name|sqlbuilder
operator|.
name|SQLBuilder
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|translator
operator|.
name|DbAttributeBinding
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
name|DbAdapter
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
name|QuotingStrategy
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
name|exp
operator|.
name|property
operator|.
name|Property
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
name|EntityResult
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
name|SQLResult
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
name|QueryMetadata
import|;
end_import

begin_comment
comment|/**  * Context that holds all data necessary for query translation as well as a result of that translation.  *  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|TranslatorContext
implements|implements
name|SQLGenerationContext
block|{
specifier|private
specifier|final
name|TableTree
name|tableTree
decl_stmt|;
comment|/**      * Result columns, can be following:      * - root object attributes (including flattened)      * - root object additional db attributes (PKs and FKs)      * - flattened attributes additional PKs      * - prefetched objects attributes and additional db attributes (PKs and FKs)      * - order by expressions if query is distinct?      */
specifier|private
specifier|final
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|columnDescriptors
decl_stmt|;
comment|/**      * Scalar values bindings in order of appearance in final SQL,      * may be should be filled by SQL node visitor.      *<p>      * Can be from expressions encountered in:      * - attributes      * - order by expressions      * - where expression (including qualifiers from all used DbEntities and ObjEntities)      */
specifier|private
specifier|final
name|Collection
argument_list|<
name|DbAttributeBinding
argument_list|>
name|bindings
decl_stmt|;
comment|// Translated query
specifier|private
specifier|final
name|TranslatableQueryWrapper
name|query
decl_stmt|;
specifier|private
specifier|final
name|QueryMetadata
name|metadata
decl_stmt|;
specifier|private
specifier|final
name|EntityResolver
name|resolver
decl_stmt|;
specifier|private
specifier|final
name|DbAdapter
name|adapter
decl_stmt|;
specifier|private
specifier|final
name|QuotingStrategy
name|quotingStrategy
decl_stmt|;
comment|// Select builder that builds final SQL tree
specifier|private
specifier|final
name|SelectBuilder
name|selectBuilder
decl_stmt|;
specifier|private
specifier|final
name|QualifierTranslator
name|qualifierTranslator
decl_stmt|;
specifier|private
specifier|final
name|PathTranslator
name|pathTranslator
decl_stmt|;
comment|// Parent context will be not null in case of a nested query
specifier|private
specifier|final
name|TranslatorContext
name|parentContext
decl_stmt|;
comment|// List of SQL tree nodes that describe resulting rows of this query
specifier|private
specifier|final
name|List
argument_list|<
name|ResultNodeDescriptor
argument_list|>
name|resultNodeList
decl_stmt|;
comment|// resulting qualifier for this query ('where' qualifier and qualifiers from entities)
specifier|private
name|Node
name|qualifierNode
decl_stmt|;
comment|// if true SQL generation stage will be skipped, needed for nested queries translation
specifier|private
name|boolean
name|skipSQLGeneration
decl_stmt|;
comment|// translated SQL string
specifier|private
name|String
name|finalSQL
decl_stmt|;
comment|// suppress DISTINCT clause
specifier|private
name|boolean
name|distinctSuppression
decl_stmt|;
comment|// index of a last result node of a root entity
specifier|private
name|int
name|rootSegmentEnd
decl_stmt|;
comment|// should current entity be linked to root object
comment|// (prefetch entity should, while unrelated entity in a column select shouldn't)
comment|// this flag can be removed if logic that converts result row into an object tree allows random order of columns in a row.
specifier|private
name|boolean
name|appendResultToRoot
decl_stmt|;
specifier|private
name|SQLResult
name|sqlResult
decl_stmt|;
specifier|private
name|EntityResult
name|rootEntityResult
decl_stmt|;
name|TranslatorContext
parameter_list|(
name|TranslatableQueryWrapper
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|TranslatorContext
name|parentContext
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|metadata
operator|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|this
operator|.
name|parentContext
operator|=
name|parentContext
expr_stmt|;
name|this
operator|.
name|tableTree
operator|=
operator|new
name|TableTree
argument_list|(
name|metadata
operator|.
name|getDbEntity
argument_list|()
argument_list|,
name|parentContext
operator|==
literal|null
condition|?
literal|null
else|:
name|parentContext
operator|.
name|getTableTree
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|columnDescriptors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|bindings
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|this
operator|.
name|selectBuilder
operator|=
name|SQLBuilder
operator|.
name|select
argument_list|()
expr_stmt|;
name|this
operator|.
name|pathTranslator
operator|=
operator|new
name|PathTranslator
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|qualifierTranslator
operator|=
operator|new
name|QualifierTranslator
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|quotingStrategy
operator|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
expr_stmt|;
name|this
operator|.
name|resultNodeList
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
if|if
condition|(
name|query
operator|.
name|needsResultSetMapping
argument_list|()
condition|)
block|{
name|this
operator|.
name|sqlResult
operator|=
operator|new
name|SQLResult
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Mark start of a new class descriptor, to be able to process result columns properly.      * @param type of a descriptor      * @see #addResultNode(Node, boolean, Property, String)      */
name|void
name|markDescriptorStart
parameter_list|(
name|DescriptorType
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
name|DescriptorType
operator|.
name|PREFETCH
condition|)
block|{
name|appendResultToRoot
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|void
name|markDescriptorEnd
parameter_list|(
name|DescriptorType
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
name|DescriptorType
operator|.
name|ROOT
condition|)
block|{
name|rootSegmentEnd
operator|=
name|resultNodeList
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
block|}
if|else if
condition|(
name|type
operator|==
name|DescriptorType
operator|.
name|PREFETCH
condition|)
block|{
name|appendResultToRoot
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|SelectBuilder
name|getSelectBuilder
parameter_list|()
block|{
return|return
name|selectBuilder
return|;
block|}
name|Collection
argument_list|<
name|ColumnDescriptor
argument_list|>
name|getColumnDescriptors
parameter_list|()
block|{
return|return
name|columnDescriptors
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DbAttributeBinding
argument_list|>
name|getBindings
parameter_list|()
block|{
return|return
name|bindings
return|;
block|}
name|TableTree
name|getTableTree
parameter_list|()
block|{
return|return
name|tableTree
return|;
block|}
name|QualifierTranslator
name|getQualifierTranslator
parameter_list|()
block|{
return|return
name|qualifierTranslator
return|;
block|}
name|PathTranslator
name|getPathTranslator
parameter_list|()
block|{
return|return
name|pathTranslator
return|;
block|}
name|int
name|getTableCount
parameter_list|()
block|{
return|return
name|tableTree
operator|.
name|getNodeCount
argument_list|()
return|;
block|}
name|TranslatableQueryWrapper
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
name|QueryMetadata
name|getMetadata
parameter_list|()
block|{
return|return
name|metadata
return|;
block|}
name|EntityResolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
specifier|public
name|DbEntity
name|getRootDbEntity
parameter_list|()
block|{
return|return
name|metadata
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
specifier|public
name|QuotingStrategy
name|getQuotingStrategy
parameter_list|()
block|{
return|return
name|quotingStrategy
return|;
block|}
name|void
name|setDistinctSuppression
parameter_list|(
name|boolean
name|distinctSuppression
parameter_list|)
block|{
name|this
operator|.
name|distinctSuppression
operator|=
name|distinctSuppression
expr_stmt|;
block|}
name|boolean
name|isDistinctSuppression
parameter_list|()
block|{
return|return
name|distinctSuppression
return|;
block|}
name|void
name|setFinalSQL
parameter_list|(
name|String
name|SQL
parameter_list|)
block|{
name|this
operator|.
name|finalSQL
operator|=
name|SQL
expr_stmt|;
block|}
name|String
name|getFinalSQL
parameter_list|()
block|{
return|return
name|finalSQL
return|;
block|}
name|List
argument_list|<
name|ResultNodeDescriptor
argument_list|>
name|getResultNodeList
parameter_list|()
block|{
return|return
name|resultNodeList
return|;
block|}
name|ResultNodeDescriptor
name|addResultNode
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
return|return
name|addResultNode
argument_list|(
name|node
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
name|ResultNodeDescriptor
name|addResultNode
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|dataRowKey
parameter_list|)
block|{
return|return
name|addResultNode
argument_list|(
name|node
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
name|dataRowKey
argument_list|)
return|;
block|}
name|ResultNodeDescriptor
name|addResultNode
parameter_list|(
name|Node
name|node
parameter_list|,
name|boolean
name|inDataRow
parameter_list|,
name|Property
argument_list|<
name|?
argument_list|>
name|property
parameter_list|,
name|String
name|dataRowKey
parameter_list|)
block|{
name|ResultNodeDescriptor
name|resultNode
init|=
operator|new
name|ResultNodeDescriptor
argument_list|(
name|node
argument_list|,
name|inDataRow
argument_list|,
name|property
argument_list|,
name|dataRowKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|appendResultToRoot
condition|)
block|{
name|resultNodeList
operator|.
name|add
argument_list|(
name|rootSegmentEnd
operator|+
literal|1
argument_list|,
name|resultNode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resultNodeList
operator|.
name|add
argument_list|(
name|resultNode
argument_list|)
expr_stmt|;
block|}
return|return
name|resultNode
return|;
block|}
name|TranslatorContext
name|getParentContext
parameter_list|()
block|{
return|return
name|parentContext
return|;
block|}
name|void
name|setSkipSQLGeneration
parameter_list|(
name|boolean
name|skipSQLGeneration
parameter_list|)
block|{
name|this
operator|.
name|skipSQLGeneration
operator|=
name|skipSQLGeneration
expr_stmt|;
block|}
name|boolean
name|isSkipSQLGeneration
parameter_list|()
block|{
return|return
name|skipSQLGeneration
return|;
block|}
name|SQLResult
name|getSqlResult
parameter_list|()
block|{
return|return
name|sqlResult
return|;
block|}
name|void
name|setRootEntityResult
parameter_list|(
name|EntityResult
name|rootEntityResult
parameter_list|)
block|{
name|this
operator|.
name|rootEntityResult
operator|=
name|rootEntityResult
expr_stmt|;
block|}
name|EntityResult
name|getRootEntityResult
parameter_list|()
block|{
return|return
name|rootEntityResult
return|;
block|}
name|void
name|setQualifierNode
parameter_list|(
name|Node
name|qualifierNode
parameter_list|)
block|{
name|this
operator|.
name|qualifierNode
operator|=
name|qualifierNode
expr_stmt|;
block|}
name|Node
name|getQualifierNode
parameter_list|()
block|{
return|return
name|qualifierNode
return|;
block|}
enum|enum
name|DescriptorType
block|{
name|ROOT
block|,
name|PREFETCH
block|,
name|OTHER
block|}
block|}
end_class

end_unit

