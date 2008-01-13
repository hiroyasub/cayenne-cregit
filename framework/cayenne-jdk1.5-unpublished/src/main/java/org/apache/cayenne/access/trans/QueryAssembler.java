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
name|access
operator|.
name|QueryLogger
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
name|QueryTranslator
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
comment|/**  * Abstract superclass of Query translators.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|QueryAssembler
extends|extends
name|QueryTranslator
block|{
comment|/** PreparedStatement values. */
specifier|protected
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
argument_list|()
decl_stmt|;
comment|/**      * PreparedStatement attributes matching entries in<code>values</code> list.      */
specifier|protected
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|()
decl_stmt|;
comment|/** Processes a join being added. */
specifier|public
specifier|abstract
name|void
name|dbRelationshipAdded
parameter_list|(
name|DbRelationship
name|dbRel
parameter_list|)
function_decl|;
comment|/**      * Translates query into sql string. This is a workhorse method of QueryAssembler. It      * is called internally from<code>createStatement</code>. Usually there is no need      * to invoke it explicitly.      */
specifier|public
specifier|abstract
name|String
name|createSqlString
parameter_list|()
throws|throws
name|Exception
function_decl|;
specifier|public
name|String
name|aliasForTable
parameter_list|(
name|DbEntity
name|ent
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
name|aliasForTable
argument_list|(
name|ent
argument_list|)
return|;
comment|// Default implementation
block|}
comment|/**      * Returns a name that can be used as column alias. This can be one of the following:      *<ul>      *<li>an alias for this table, if it uses aliases</li>      *<li>a fully qualified table name, if not.</li>      *</ul>      * CayenneRuntimeException is thrown if a table alias can not be created.      */
specifier|public
specifier|abstract
name|String
name|aliasForTable
parameter_list|(
name|DbEntity
name|dbEnt
parameter_list|)
function_decl|;
comment|/**      * Returns<code>true</code> if table aliases are supported. Default implementation      * returns false.      */
specifier|public
name|boolean
name|supportsTableAliases
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Registers<code>anObject</code> as a PreparedStatement parameter.      *       * @param anObject object that represents a value of DbAttribute      * @param dbAttr DbAttribute being processed.      */
specifier|public
name|void
name|addToParamList
parameter_list|(
name|DbAttribute
name|dbAttr
parameter_list|,
name|Object
name|anObject
parameter_list|)
block|{
name|attributes
operator|.
name|add
argument_list|(
name|dbAttr
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
name|anObject
argument_list|)
expr_stmt|;
block|}
comment|/**       * Translates internal query into PreparedStatement.       */
annotation|@
name|Override
specifier|public
name|PreparedStatement
name|createStatement
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|String
name|sqlStr
init|=
name|createSqlString
argument_list|()
decl_stmt|;
name|QueryLogger
operator|.
name|logQuery
argument_list|(
name|sqlStr
argument_list|,
name|attributes
argument_list|,
name|values
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|t1
argument_list|)
expr_stmt|;
name|PreparedStatement
name|stmt
init|=
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sqlStr
argument_list|)
decl_stmt|;
name|initStatement
argument_list|(
name|stmt
argument_list|)
expr_stmt|;
return|return
name|stmt
return|;
block|}
comment|/**      * Initializes prepared statements with collected parameters. Called internally from      * "createStatement". Cayenne users shouldn't normally call it directly.      */
specifier|protected
name|void
name|initStatement
parameter_list|(
name|PreparedStatement
name|stmt
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|len
init|=
name|values
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|val
init|=
name|values
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr
init|=
name|attributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// null DbAttributes are a result of inferior qualifier processing
comment|// (qualifier can't map parameters to DbAttributes and therefore
comment|// only supports standard java types now)
comment|// hence, a special moronic case here:
if|if
condition|(
name|attr
operator|==
literal|null
condition|)
block|{
name|stmt
operator|.
name|setObject
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|adapter
operator|.
name|bindParameter
argument_list|(
name|stmt
argument_list|,
name|val
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|attr
operator|.
name|getType
argument_list|()
argument_list|,
name|attr
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

