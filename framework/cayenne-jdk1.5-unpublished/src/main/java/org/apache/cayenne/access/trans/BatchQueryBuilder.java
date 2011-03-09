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
name|trans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
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
name|sql
operator|.
name|Types
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
name|query
operator|.
name|BatchQuery
import|;
end_import

begin_comment
comment|/**  * Superclass of batch query translators.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BatchQueryBuilder
block|{
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|String
name|trimFunction
decl_stmt|;
comment|/**      * @deprecated since 3.1 unused      */
specifier|public
name|BatchQueryBuilder
parameter_list|()
block|{
block|}
specifier|public
name|BatchQueryBuilder
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
comment|/**      * Translates BatchQuery into an SQL string formatted to use in a PreparedStatement.      *       * @throws IOException      */
specifier|public
specifier|abstract
name|String
name|createSqlString
parameter_list|(
name|BatchQuery
name|batch
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Appends the name of the column to the query buffer. Subclasses use this method to      * append column names in the WHERE clause, i.e. for the columns that are not being      * updated.      */
specifier|protected
name|void
name|appendDbAttribute
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|)
block|{
comment|// TODO: (Andrus) is there a need for trimming binary types?
name|boolean
name|trim
init|=
name|dbAttribute
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CHAR
operator|&&
name|trimFunction
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|trim
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|trimFunction
argument_list|)
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
block|}
name|boolean
name|status
decl_stmt|;
if|if
condition|(
name|dbAttribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|dbAttribute
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
condition|)
block|{
name|status
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|status
operator|=
literal|false
expr_stmt|;
block|}
name|QuotingStrategy
name|strategy
init|=
name|getAdapter
argument_list|()
operator|.
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quoteString
argument_list|(
name|dbAttribute
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|trim
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setAdapter
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
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
name|String
name|getTrimFunction
parameter_list|()
block|{
return|return
name|trimFunction
return|;
block|}
specifier|public
name|void
name|setTrimFunction
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|trimFunction
operator|=
name|string
expr_stmt|;
block|}
comment|/**      * Binds parameters for the current batch iteration to the PreparedStatement.      *       * @since 1.2      */
specifier|public
name|void
name|bindParameters
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|BatchQuery
name|query
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
init|=
name|query
operator|.
name|getDbAttributes
argument_list|()
decl_stmt|;
name|int
name|attributeCount
init|=
name|dbAttributes
operator|.
name|size
argument_list|()
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
name|attributeCount
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|value
init|=
name|query
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|adapter
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|value
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|,
name|attribute
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a list of values for the current batch iteration. Used primarily for      * logging.      *       * @since 1.2      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getParameterValues
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
block|{
name|int
name|len
init|=
name|query
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|len
argument_list|)
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|values
operator|.
name|add
argument_list|(
name|query
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|values
return|;
block|}
block|}
end_class

end_unit

