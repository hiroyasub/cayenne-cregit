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
name|gen
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
name|dbsync
operator|.
name|reverse
operator|.
name|naming
operator|.
name|NameConverter
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
name|QueryDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|VelocityContext
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

begin_comment
comment|/**  * {@link Artifact} facade for a DataMap.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|DataMapArtifact
implements|implements
name|Artifact
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DATAMAP_UTILS_KEY
init|=
literal|"dataMapUtils"
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|selectQueries
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|sqlTemplateQueries
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|procedureQueries
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|ejbqlQueries
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|queryNames
decl_stmt|;
specifier|public
name|DataMapArtifact
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|queries
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
name|selectQueries
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
name|sqlTemplateQueries
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
name|procedureQueries
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
name|ejbqlQueries
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
name|queryNames
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
name|addQueries
argument_list|(
name|queries
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getQualifiedBaseClassName
parameter_list|()
block|{
return|return
name|Object
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getQualifiedClassName
parameter_list|()
block|{
return|return
name|dataMap
operator|.
name|getNameWithDefaultPackage
argument_list|(
name|NameConverter
operator|.
name|underscoredToJava
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|this
return|;
block|}
specifier|public
name|void
name|postInitContext
parameter_list|(
name|VelocityContext
name|context
parameter_list|)
block|{
name|DataMapUtils
name|dataMapUtils
init|=
operator|new
name|DataMapUtils
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|DATAMAP_UTILS_KEY
argument_list|,
name|dataMapUtils
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TemplateType
index|[]
name|getTemplateTypes
parameter_list|(
name|ArtifactGenerationMode
name|mode
parameter_list|)
block|{
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|SINGLE_CLASS
case|:
return|return
operator|new
name|TemplateType
index|[]
block|{
name|TemplateType
operator|.
name|DATAMAP_SINGLE_CLASS
block|}
return|;
case|case
name|GENERATION_GAP
case|:
return|return
operator|new
name|TemplateType
index|[]
block|{
name|TemplateType
operator|.
name|DATAMAP_SUPERCLASS
block|,
name|TemplateType
operator|.
name|DATAMAP_SUBCLASS
block|}
return|;
default|default:
return|return
operator|new
name|TemplateType
index|[
literal|0
index|]
return|;
block|}
block|}
specifier|private
name|void
name|addQueries
parameter_list|(
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|queries
parameter_list|)
block|{
if|if
condition|(
name|queries
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|QueryDescriptor
name|query
range|:
name|queries
control|)
block|{
name|addQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addQuery
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|)
block|{
switch|switch
condition|(
name|query
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|QueryDescriptor
operator|.
name|SELECT_QUERY
case|:
name|selectQueries
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
break|break;
case|case
name|QueryDescriptor
operator|.
name|PROCEDURE_QUERY
case|:
name|procedureQueries
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
break|break;
case|case
name|QueryDescriptor
operator|.
name|SQL_TEMPLATE
case|:
name|sqlTemplateQueries
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
break|break;
case|case
name|QueryDescriptor
operator|.
name|EJBQL_QUERY
case|:
name|ejbqlQueries
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|query
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|queryNames
operator|.
name|add
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|getSelectQueries
parameter_list|()
block|{
return|return
name|selectQueries
return|;
block|}
specifier|public
name|boolean
name|hasSelectQueries
parameter_list|()
block|{
return|return
name|selectQueries
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
specifier|public
name|boolean
name|hasQueryNames
parameter_list|()
block|{
return|return
operator|!
name|queryNames
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getQueryNames
parameter_list|()
block|{
return|return
name|queryNames
return|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
block|}
end_class

end_unit

