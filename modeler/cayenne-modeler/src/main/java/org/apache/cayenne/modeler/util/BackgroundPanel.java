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
name|modeler
operator|.
name|util
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ImageIcon
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_class
specifier|public
class|class
name|BackgroundPanel
extends|extends
name|JPanel
block|{
specifier|private
name|Image
name|backgroundImage
decl_stmt|;
specifier|public
name|BackgroundPanel
parameter_list|(
name|String
name|imagePath
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|ImageIcon
name|imageIcon
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
name|imagePath
argument_list|)
decl_stmt|;
name|backgroundImage
operator|=
name|imageIcon
operator|.
name|getImage
argument_list|()
expr_stmt|;
name|Dimension
name|dimension
init|=
operator|new
name|Dimension
argument_list|(
name|imageIcon
operator|.
name|getIconWidth
argument_list|()
argument_list|,
name|imageIcon
operator|.
name|getIconHeight
argument_list|()
argument_list|)
decl_stmt|;
name|setSize
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
name|setPreferredSize
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
name|setOpaque
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|paintComponent
parameter_list|(
name|Graphics
name|g
parameter_list|)
block|{
name|super
operator|.
name|paintComponent
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|g
operator|.
name|drawImage
argument_list|(
name|backgroundImage
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

