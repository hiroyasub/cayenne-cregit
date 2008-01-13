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
name|Types
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
name|DbRelationship
import|;
end_import

begin_comment
comment|/**   * QualifierTranslator that allows translation of qualifiers that perform  * comparison with CHAR columns. Some databases require trimming the values for  * this to work.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|TrimmingQualifierTranslator
extends|extends
name|QualifierTranslator
block|{
specifier|protected
name|String
name|trimFunction
decl_stmt|;
comment|/**      * Constructor for TrimmingQualifierTranslator.      */
specifier|protected
name|TrimmingQualifierTranslator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor for TrimmingQualifierTranslator.      */
specifier|public
name|TrimmingQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|,
name|String
name|trimFunction
parameter_list|)
block|{
name|super
argument_list|(
name|queryAssembler
argument_list|)
expr_stmt|;
name|this
operator|.
name|trimFunction
operator|=
name|trimFunction
expr_stmt|;
block|}
comment|/**      * Adds special handling of CHAR columns.      */
annotation|@
name|Override
specifier|protected
name|void
name|processColumn
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|DbAttribute
name|dbAttr
parameter_list|)
block|{
if|if
condition|(
name|dbAttr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CHAR
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
literal|"("
argument_list|)
expr_stmt|;
name|super
operator|.
name|processColumn
argument_list|(
name|buf
argument_list|,
name|dbAttr
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|processColumn
argument_list|(
name|buf
argument_list|,
name|dbAttr
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds special handling of CHAR columns.      */
annotation|@
name|Override
specifier|protected
name|void
name|processColumn
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|DbAttribute
name|dbAttr
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
if|if
condition|(
name|dbAttr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CHAR
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
literal|"("
argument_list|)
expr_stmt|;
name|super
operator|.
name|processColumn
argument_list|(
name|buf
argument_list|,
name|dbAttr
argument_list|,
name|rel
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|processColumn
argument_list|(
name|buf
argument_list|,
name|dbAttr
argument_list|,
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the trimFunction.      * @return String      */
specifier|public
name|String
name|getTrimFunction
parameter_list|()
block|{
return|return
name|trimFunction
return|;
block|}
comment|/**      * Sets the trimFunction.      * @param trimFunction The trimFunction to set      */
specifier|public
name|void
name|setTrimFunction
parameter_list|(
name|String
name|trimFunction
parameter_list|)
block|{
name|this
operator|.
name|trimFunction
operator|=
name|trimFunction
expr_stmt|;
block|}
block|}
end_class

end_unit

