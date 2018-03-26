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
name|modeler
operator|.
name|dialog
operator|.
name|db
operator|.
name|load
package|;
end_package

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|IncludeTablePopUpMenu
extends|extends
name|RootPopUpMenu
block|{
specifier|public
name|IncludeTablePopUpMenu
parameter_list|()
block|{
name|rename
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|delete
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addCatalog
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addSchema
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addIncludeTable
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addExcludeTable
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addIncludeProcedure
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addExcludeProcedure
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

