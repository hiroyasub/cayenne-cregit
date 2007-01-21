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
name|DbRelationship
import|;
end_import

begin_comment
comment|/** Class implements default translation mechanism of org.apache.cayenne.query.DeleteQuery  *  objects to SQL DELETE statements.  *  *  @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DeleteTranslator
extends|extends
name|QueryAssembler
block|{
specifier|public
name|String
name|aliasForTable
parameter_list|(
name|DbEntity
name|dbEnt
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"aliases not supported"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|dbRelationshipAdded
parameter_list|(
name|DbRelationship
name|dbRel
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"db relationships not supported"
argument_list|)
throw|;
block|}
comment|/** Main method of DeleteTranslator class. Translates DeleteQuery      *  into a JDBC PreparedStatement      */
specifier|public
name|String
name|createSqlString
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|queryBuf
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"DELETE FROM "
argument_list|)
decl_stmt|;
comment|// 1. append table name
name|DbEntity
name|dbEnt
init|=
name|getRootDbEntity
argument_list|()
decl_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
name|dbEnt
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
comment|// 2. build qualifier
name|String
name|qualifierStr
init|=
name|adapter
operator|.
name|getQualifierTranslator
argument_list|(
name|this
argument_list|)
operator|.
name|doTranslation
argument_list|()
decl_stmt|;
if|if
condition|(
name|qualifierStr
operator|!=
literal|null
condition|)
name|queryBuf
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
operator|.
name|append
argument_list|(
name|qualifierStr
argument_list|)
expr_stmt|;
return|return
name|queryBuf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

