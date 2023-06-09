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
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSetMetaData
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|TypesMapping
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
name|DbAttribute
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
name|ObjAttribute
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
name|ProcedureParameter
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
name|EqualsBuilder
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
name|HashCodeBuilder
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
name|ToStringBuilder
import|;
end_import

begin_comment
comment|/**  * A descriptor of a ResultSet column.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ColumnDescriptor
block|{
specifier|protected
name|DbAttribute
name|attribute
decl_stmt|;
specifier|protected
name|String
name|tableName
decl_stmt|;
specifier|protected
name|String
name|procedureName
decl_stmt|;
comment|// identifies column in result set
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|namePrefix
decl_stmt|;
comment|// identifies column in a DataRow
specifier|protected
name|String
name|dataRowKey
decl_stmt|;
specifier|protected
name|int
name|jdbcType
decl_stmt|;
specifier|protected
name|String
name|javaClass
decl_stmt|;
comment|/**      * @since 4.0      */
specifier|protected
name|boolean
name|isExpression
decl_stmt|;
comment|/**      * Creates a ColumnDescriptor      */
specifier|public
name|ColumnDescriptor
parameter_list|()
block|{
block|}
comment|/**      * Creates a column descriptor with user-specified parameters.      *       * @since 3.1      */
specifier|public
name|ColumnDescriptor
parameter_list|(
name|String
name|columnName
parameter_list|,
name|int
name|jdbcType
parameter_list|)
block|{
name|this
argument_list|(
name|columnName
argument_list|,
name|jdbcType
argument_list|,
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|jdbcType
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|ColumnDescriptor
parameter_list|(
name|String
name|columnName
parameter_list|,
name|int
name|jdbcType
parameter_list|,
name|String
name|javaClass
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|columnName
expr_stmt|;
name|this
operator|.
name|dataRowKey
operator|=
name|columnName
expr_stmt|;
name|this
operator|.
name|jdbcType
operator|=
name|jdbcType
expr_stmt|;
name|this
operator|.
name|javaClass
operator|=
name|javaClass
expr_stmt|;
block|}
comment|/**      * Creates a ColumnDescriptor from Cayenne DbAttribute.      *       * @since 1.2      */
specifier|public
name|ColumnDescriptor
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|String
name|tableAlias
parameter_list|)
block|{
name|this
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
name|this
operator|.
name|namePrefix
operator|=
name|tableAlias
expr_stmt|;
name|this
operator|.
name|javaClass
operator|=
name|attribute
operator|.
name|getJavaClass
argument_list|()
expr_stmt|;
if|if
condition|(
name|attribute
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|tableName
operator|=
name|attribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @since 1.2      */
specifier|public
name|ColumnDescriptor
parameter_list|(
name|ObjAttribute
name|objAttribute
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|String
name|columnAlias
parameter_list|)
block|{
name|this
argument_list|(
name|dbAttribute
argument_list|,
name|columnAlias
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataRowKey
operator|=
name|objAttribute
operator|.
name|getDbAttributePath
argument_list|()
expr_stmt|;
name|this
operator|.
name|javaClass
operator|=
name|objAttribute
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates a ColumnDescriptor from stored procedure parameter.      *       * @since 1.2      */
specifier|public
name|ColumnDescriptor
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|this
argument_list|(
name|parameter
operator|.
name|getName
argument_list|()
argument_list|,
name|parameter
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|parameter
operator|.
name|getProcedure
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|procedureName
operator|=
name|parameter
operator|.
name|getProcedure
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Creates a ColumnDescriptor using ResultSetMetaData.      *       * @since 3.1      */
specifier|public
name|ColumnDescriptor
parameter_list|(
name|String
name|columnName
parameter_list|,
name|ResultSetMetaData
name|metaData
parameter_list|,
name|int
name|position
parameter_list|)
throws|throws
name|SQLException
block|{
name|this
argument_list|(
name|columnName
argument_list|,
name|metaData
operator|.
name|getColumnType
argument_list|(
name|position
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a ColumnDescriptor using ResultSetMetaData.      *       * @since 1.2      */
specifier|public
name|ColumnDescriptor
parameter_list|(
name|ResultSetMetaData
name|metaData
parameter_list|,
name|int
name|position
parameter_list|)
throws|throws
name|SQLException
block|{
name|this
argument_list|(
name|getColumnNameFromMeta
argument_list|(
name|metaData
argument_list|,
name|position
argument_list|)
argument_list|,
name|metaData
argument_list|,
name|position
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getColumnNameFromMeta
parameter_list|(
name|ResultSetMetaData
name|metaData
parameter_list|,
name|int
name|position
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|name
init|=
name|metaData
operator|.
name|getColumnLabel
argument_list|(
name|position
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
name|metaData
operator|.
name|getColumnName
argument_list|(
name|position
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|name
operator|=
literal|"column_"
operator|+
name|position
expr_stmt|;
block|}
block|}
return|return
name|name
return|;
block|}
comment|/**      * Returns a DbAttribute for this column. Since columns descriptors can be      * initialized in a context where a DbAttribite is unknown, this method may      * return null.      *       * @since 4.0      */
specifier|public
name|DbAttribute
name|getAttribute
parameter_list|()
block|{
return|return
name|attribute
return|;
block|}
specifier|public
name|void
name|setAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
block|}
comment|/**      * Returns true if another object is a ColumnDescriptor with the same name,      * name prefix, table and procedure names. Other fields are ignored in the      * equality test.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|ColumnDescriptor
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ColumnDescriptor
name|rhs
init|=
operator|(
name|ColumnDescriptor
operator|)
name|o
decl_stmt|;
return|return
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|name
argument_list|,
name|rhs
operator|.
name|name
argument_list|)
operator|.
name|append
argument_list|(
name|namePrefix
argument_list|,
name|rhs
operator|.
name|namePrefix
argument_list|)
operator|.
name|append
argument_list|(
name|procedureName
argument_list|,
name|rhs
operator|.
name|procedureName
argument_list|)
operator|.
name|append
argument_list|(
name|dataRowKey
argument_list|,
name|rhs
operator|.
name|dataRowKey
argument_list|)
operator|.
name|append
argument_list|(
name|tableName
argument_list|,
name|rhs
operator|.
name|tableName
argument_list|)
operator|.
name|isEquals
argument_list|()
return|;
block|}
comment|/**      * @since 1.2      */
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|new
name|HashCodeBuilder
argument_list|(
literal|23
argument_list|,
literal|43
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
name|namePrefix
argument_list|)
operator|.
name|append
argument_list|(
name|procedureName
argument_list|)
operator|.
name|append
argument_list|(
name|tableName
argument_list|)
operator|.
name|append
argument_list|(
name|dataRowKey
argument_list|)
operator|.
name|toHashCode
argument_list|()
return|;
block|}
comment|/**      * @since 1.2      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|ToStringBuilder
name|builder
init|=
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"namePrefix"
argument_list|,
name|namePrefix
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"dataRowKey"
argument_list|,
name|getDataRowKey
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"tableName"
argument_list|,
name|getTableName
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"procedureName"
argument_list|,
name|getProcedureName
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"javaClass"
argument_list|,
name|getJavaClass
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"jdbcType"
argument_list|,
name|getJdbcType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns "qualifiedColumnName" property.      *       * @since 1.2      */
specifier|public
name|String
name|getQualifiedColumnName
parameter_list|()
block|{
return|return
operator|(
name|namePrefix
operator|!=
literal|null
operator|)
condition|?
name|namePrefix
operator|+
literal|'.'
operator|+
name|name
else|:
name|name
return|;
block|}
specifier|public
name|int
name|getJdbcType
parameter_list|()
block|{
return|return
name|jdbcType
return|;
block|}
comment|/**      * Returns column name. Name is an unqualified column name in a query.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|String
name|getNamePrefix
parameter_list|()
block|{
return|return
name|namePrefix
return|;
block|}
specifier|public
name|void
name|setJdbcType
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|jdbcType
operator|=
name|i
expr_stmt|;
block|}
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
specifier|public
name|String
name|getJavaClass
parameter_list|()
block|{
return|return
name|javaClass
return|;
block|}
specifier|public
name|void
name|setJavaClass
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|javaClass
operator|=
name|string
expr_stmt|;
block|}
comment|/**      * Returns the name of the parent table.      *       * @since 1.2      */
specifier|public
name|String
name|getTableName
parameter_list|()
block|{
return|return
name|tableName
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
block|}
comment|/**      * Returns the name of the parent stored procedure.      *       * @since 1.2      */
specifier|public
name|String
name|getProcedureName
parameter_list|()
block|{
return|return
name|procedureName
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setProcedureName
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
name|this
operator|.
name|procedureName
operator|=
name|procedureName
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
name|getDataRowKey
parameter_list|()
block|{
return|return
name|dataRowKey
operator|!=
literal|null
condition|?
name|dataRowKey
else|:
name|getName
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setDataRowKey
parameter_list|(
name|String
name|dataRowKey
parameter_list|)
block|{
name|this
operator|.
name|dataRowKey
operator|=
name|dataRowKey
expr_stmt|;
block|}
comment|/**      * @since 4.0      * @param isExpression is this column is an expression      */
specifier|public
name|void
name|setIsExpression
parameter_list|(
name|boolean
name|isExpression
parameter_list|)
block|{
name|this
operator|.
name|isExpression
operator|=
name|isExpression
expr_stmt|;
block|}
comment|/**      * @since 4.0      * @return is this column is an expression      */
specifier|public
name|boolean
name|isExpression
parameter_list|()
block|{
return|return
name|isExpression
return|;
block|}
block|}
end_class

end_unit

