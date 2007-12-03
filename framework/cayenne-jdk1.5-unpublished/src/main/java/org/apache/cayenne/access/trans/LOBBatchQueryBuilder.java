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
name|query
operator|.
name|BatchQuery
import|;
end_import

begin_comment
comment|/**  * Superclass of query builders for the DML operations involving LOBs.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|LOBBatchQueryBuilder
extends|extends
name|BatchQueryBuilder
block|{
specifier|protected
name|String
name|newClobFunction
decl_stmt|;
specifier|protected
name|String
name|newBlobFunction
decl_stmt|;
specifier|public
name|LOBBatchQueryBuilder
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|List
name|getValuesForLOBUpdateParameters
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
function_decl|;
specifier|public
name|String
name|createLOBSelectString
parameter_list|(
name|BatchQuery
name|updateQuery
parameter_list|,
name|List
name|selectedLOBAttributes
parameter_list|,
name|List
name|qualifierAttributes
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"SELECT "
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|selectedLOBAttributes
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
name|buf
operator|.
name|append
argument_list|(
operator|(
operator|(
name|DbAttribute
operator|)
name|it
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|buf
operator|.
name|append
argument_list|(
literal|" FROM "
argument_list|)
operator|.
name|append
argument_list|(
name|updateQuery
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|it
operator|=
name|qualifierAttributes
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|appendDbAttribute
argument_list|(
name|buf
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" = ?"
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
block|}
name|buf
operator|.
name|append
argument_list|(
literal|" FOR UPDATE"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Appends parameter placeholder for the value of the column being updated. If      * requested, performs special handling on LOB columns.      */
specifier|protected
name|void
name|appendUpdatedParameter
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|int
name|type
init|=
name|dbAttribute
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|isUpdateableColumn
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|newClobFunction
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|newBlobFunction
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unknown LOB column type: "
operator|+
name|type
operator|+
literal|"("
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
operator|+
literal|"). Query buffer: "
operator|+
name|buf
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Binds BatchQuery parameters to the PreparedStatement.      */
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
comment|// i - attribute position in the query
comment|// j - PreparedStatement parameter position (starts with "1")
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
literal|1
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
name|int
name|type
init|=
name|attribute
operator|.
name|getType
argument_list|()
decl_stmt|;
comment|// TODO: (Andrus) This works as long as there is no LOBs in qualifier
if|if
condition|(
name|isUpdateableColumn
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
condition|)
block|{
name|adapter
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|value
argument_list|,
name|j
argument_list|,
name|type
argument_list|,
name|attribute
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
name|j
operator|++
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|boolean
name|isUpdateableColumn
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|type
parameter_list|)
block|{
return|return
name|value
operator|==
literal|null
operator|||
operator|(
name|type
operator|!=
name|Types
operator|.
name|BLOB
operator|&&
name|type
operator|!=
name|Types
operator|.
name|CLOB
operator|)
return|;
block|}
specifier|public
name|String
name|getNewBlobFunction
parameter_list|()
block|{
return|return
name|newBlobFunction
return|;
block|}
specifier|public
name|String
name|getNewClobFunction
parameter_list|()
block|{
return|return
name|newClobFunction
return|;
block|}
specifier|public
name|void
name|setNewBlobFunction
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|newBlobFunction
operator|=
name|string
expr_stmt|;
block|}
specifier|public
name|void
name|setNewClobFunction
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|newClobFunction
operator|=
name|string
expr_stmt|;
block|}
block|}
end_class

end_unit

