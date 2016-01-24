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
name|exp
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|TstExpressionCase
block|{
specifier|protected
name|int
name|totalNodes
decl_stmt|;
specifier|protected
name|int
name|totalLeaves
decl_stmt|;
specifier|protected
name|Expression
name|cayenneExp
decl_stmt|;
specifier|protected
name|String
name|sqlExp
decl_stmt|;
specifier|protected
name|String
name|rootEntity
decl_stmt|;
specifier|public
name|TstExpressionCase
parameter_list|(
name|String
name|rootEntity
parameter_list|,
name|Expression
name|cayenneExp
parameter_list|,
name|String
name|sqlExp
parameter_list|,
name|int
name|totalNodes
parameter_list|,
name|int
name|totalLeaves
parameter_list|)
block|{
name|this
operator|.
name|cayenneExp
operator|=
name|cayenneExp
expr_stmt|;
name|this
operator|.
name|rootEntity
operator|=
name|rootEntity
expr_stmt|;
name|this
operator|.
name|sqlExp
operator|=
name|sqlExp
expr_stmt|;
name|this
operator|.
name|totalNodes
operator|=
name|totalNodes
expr_stmt|;
name|this
operator|.
name|totalLeaves
operator|=
name|totalLeaves
expr_stmt|;
block|}
specifier|public
name|int
name|getTotalNodes
parameter_list|()
block|{
return|return
name|totalNodes
return|;
block|}
specifier|public
name|int
name|getTotalLeaves
parameter_list|()
block|{
return|return
name|totalLeaves
return|;
block|}
specifier|public
name|Expression
name|getCayenneExp
parameter_list|()
block|{
return|return
name|cayenneExp
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
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
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|cayenneExp
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|assertTranslatedWell
parameter_list|(
name|String
name|translated
parameter_list|)
block|{
if|if
condition|(
name|sqlExp
operator|==
literal|null
condition|)
block|{
name|assertNull
argument_list|(
name|translated
argument_list|)
expr_stmt|;
return|return;
block|}
name|assertNotNull
argument_list|(
name|translated
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected translation: "
operator|+
name|translated
operator|+
literal|"...."
argument_list|,
name|sqlExp
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRootEntity
parameter_list|()
block|{
return|return
name|rootEntity
return|;
block|}
specifier|public
name|String
name|getSqlExp
parameter_list|()
block|{
return|return
name|sqlExp
return|;
block|}
block|}
end_class

end_unit

