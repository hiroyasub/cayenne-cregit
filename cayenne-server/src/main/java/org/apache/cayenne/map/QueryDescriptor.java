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
name|map
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
name|configuration
operator|.
name|ConfigurationNode
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
name|ConfigurationNodeVisitor
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
name|util
operator|.
name|XMLEncoder
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
name|XMLSerializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Generic descriptor of a Cayenne query.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|QueryDescriptor
implements|implements
name|Serializable
implements|,
name|ConfigurationNode
implements|,
name|XMLSerializable
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SELECT_QUERY
init|=
literal|"SelectQuery"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SQL_TEMPLATE
init|=
literal|"SQLTemplate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EJBQL_QUERY
init|=
literal|"EJBQLQuery"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROCEDURE_QUERY
init|=
literal|"ProcedureQuery"
decl_stmt|;
comment|/**      * @since 4.1      */
specifier|public
specifier|static
specifier|final
name|String
name|OBJ_ENTITY_ROOT
init|=
literal|"obj-entity"
decl_stmt|;
comment|/**      * @since 4.1      */
specifier|public
specifier|static
specifier|final
name|String
name|DB_ENTITY_ROOT
init|=
literal|"db-entity"
decl_stmt|;
comment|/**      * @since 4.1      */
specifier|public
specifier|static
specifier|final
name|String
name|PROCEDURE_ROOT
init|=
literal|"procedure"
decl_stmt|;
comment|/**      * @since 4.1      */
specifier|public
specifier|static
specifier|final
name|String
name|DATA_MAP_ROOT
init|=
literal|"data-map"
decl_stmt|;
comment|/**      * @since 4.1      */
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_CLASS_ROOT
init|=
literal|"java-class"
decl_stmt|;
comment|/**      * Creates new SelectQuery query descriptor.      */
specifier|public
specifier|static
name|SelectQueryDescriptor
name|selectQueryDescriptor
parameter_list|()
block|{
return|return
operator|new
name|SelectQueryDescriptor
argument_list|()
return|;
block|}
comment|/**      * Creates new SQLTemplate query descriptor.      */
specifier|public
specifier|static
name|SQLTemplateDescriptor
name|sqlTemplateDescriptor
parameter_list|()
block|{
return|return
operator|new
name|SQLTemplateDescriptor
argument_list|()
return|;
block|}
comment|/**      * Creates new ProcedureQuery query descriptor.      */
specifier|public
specifier|static
name|ProcedureQueryDescriptor
name|procedureQueryDescriptor
parameter_list|()
block|{
return|return
operator|new
name|ProcedureQueryDescriptor
argument_list|()
return|;
block|}
comment|/**      * Creates new EJBQLQuery query descriptor.      */
specifier|public
specifier|static
name|EJBQLQueryDescriptor
name|ejbqlQueryDescriptor
parameter_list|()
block|{
return|return
operator|new
name|EJBQLQueryDescriptor
argument_list|()
return|;
block|}
comment|/**      * Creates query descriptor of a given type.      */
specifier|public
specifier|static
name|QueryDescriptor
name|descriptor
parameter_list|(
name|String
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|SELECT_QUERY
case|:
return|return
name|selectQueryDescriptor
argument_list|()
return|;
case|case
name|SQL_TEMPLATE
case|:
return|return
name|sqlTemplateDescriptor
argument_list|()
return|;
case|case
name|EJBQL_QUERY
case|:
return|return
name|ejbqlQueryDescriptor
argument_list|()
return|;
case|case
name|PROCEDURE_QUERY
case|:
return|return
name|procedureQueryDescriptor
argument_list|()
return|;
default|default:
return|return
operator|new
name|QueryDescriptor
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|type
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|Object
name|root
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|QueryDescriptor
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Returns name of the query.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Sets name of the query.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Returns type of the query.      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Sets type of the query.      */
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
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
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/**      * Returns the root of this query.      */
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * Sets the root of this query.      */
specifier|public
name|void
name|setRoot
parameter_list|(
name|Object
name|root
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
block|}
comment|/**      * Returns map of query properties set up for this query.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * Returns query property by its name.      */
specifier|public
name|String
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Sets map of query properties for this query.      */
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
comment|/**      * Sets single query property.      */
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Assembles Cayenne query instance of appropriate type from this descriptor.      */
specifier|public
name|Query
name|buildQuery
parameter_list|()
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to build query object of this type."
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitQuery
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"query"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|String
name|rootString
init|=
literal|null
decl_stmt|;
name|String
name|rootType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|root
operator|instanceof
name|String
condition|)
block|{
name|rootType
operator|=
name|OBJ_ENTITY_ROOT
expr_stmt|;
name|rootString
operator|=
name|root
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|ObjEntity
condition|)
block|{
name|rootType
operator|=
name|OBJ_ENTITY_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|ObjEntity
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|DbEntity
condition|)
block|{
name|rootType
operator|=
name|DB_ENTITY_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|DbEntity
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|Procedure
condition|)
block|{
name|rootType
operator|=
name|PROCEDURE_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|Procedure
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|rootType
operator|=
name|JAVA_CLASS_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|rootType
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|attribute
argument_list|(
literal|"root"
argument_list|,
name|rootType
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"root-name"
argument_list|,
name|rootString
argument_list|)
expr_stmt|;
block|}
name|encodeProperties
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|visitQuery
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
name|void
name|encodeProperties
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|property
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|property
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|encoder
operator|.
name|property
argument_list|(
name|property
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

