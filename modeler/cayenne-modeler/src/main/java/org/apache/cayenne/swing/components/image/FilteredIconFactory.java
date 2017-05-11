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
name|swing
operator|.
name|components
operator|.
name|image
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|FilteredImageSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|ImageProducer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|RGBImageFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|Icon
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComponent
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
name|javax
operator|.
name|swing
operator|.
name|UIManager
import|;
end_import

begin_import
import|import
name|sun
operator|.
name|swing
operator|.
name|ImageIconUIResource
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|FilteredIconFactory
block|{
specifier|private
specifier|static
specifier|final
name|JComponent
name|DUMMY
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
specifier|public
enum|enum
name|FilterType
block|{
name|DISABLE
argument_list|(
operator|new
name|DisabledFilter
argument_list|()
argument_list|)
block|,
name|SELECTION
argument_list|(
operator|new
name|SelectionFilter
argument_list|()
argument_list|)
block|,
name|WHITE
argument_list|(
operator|new
name|ColorFilter
argument_list|(
literal|0xFFFFFF
argument_list|)
argument_list|)
block|,
name|GREEN
argument_list|(
operator|new
name|ColorFilter
argument_list|(
literal|0x65A91B
argument_list|)
argument_list|)
block|,
name|VIOLET
argument_list|(
operator|new
name|ColorFilter
argument_list|(
literal|0xAD78DD
argument_list|)
argument_list|)
block|,
name|BLUE
argument_list|(
operator|new
name|ColorFilter
argument_list|(
literal|0x53A3D6
argument_list|)
argument_list|)
block|,
name|GRAY
argument_list|(
operator|new
name|ColorFilter
argument_list|(
literal|0x434343
argument_list|)
argument_list|)
block|;
specifier|private
specifier|final
name|RGBImageFilter
name|filter
decl_stmt|;
name|FilterType
parameter_list|(
name|RGBImageFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
block|}
specifier|static
specifier|public
name|Icon
name|createDisabledIcon
parameter_list|(
name|Icon
name|icon
parameter_list|)
block|{
return|return
name|createIcon
argument_list|(
name|icon
argument_list|,
name|FilterType
operator|.
name|DISABLE
argument_list|)
return|;
block|}
specifier|static
specifier|public
name|Icon
name|createIcon
parameter_list|(
name|Icon
name|icon
parameter_list|,
name|FilterType
name|filterType
parameter_list|)
block|{
if|if
condition|(
name|icon
operator|!=
literal|null
operator|&&
name|icon
operator|.
name|getIconWidth
argument_list|()
operator|>
literal|0
operator|&&
name|icon
operator|.
name|getIconHeight
argument_list|()
operator|>
literal|0
condition|)
block|{
name|BufferedImage
name|img
init|=
operator|new
name|BufferedImage
argument_list|(
name|icon
operator|.
name|getIconWidth
argument_list|()
argument_list|,
name|icon
operator|.
name|getIconWidth
argument_list|()
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|)
decl_stmt|;
name|icon
operator|.
name|paintIcon
argument_list|(
name|DUMMY
argument_list|,
name|img
operator|.
name|getGraphics
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|ImageProducer
name|producer
init|=
operator|new
name|FilteredImageSource
argument_list|(
name|img
operator|.
name|getSource
argument_list|()
argument_list|,
name|filterType
operator|.
name|filter
argument_list|)
decl_stmt|;
name|Image
name|resultImage
init|=
name|DUMMY
operator|.
name|createImage
argument_list|(
name|producer
argument_list|)
decl_stmt|;
return|return
operator|new
name|ImageIconUIResource
argument_list|(
name|resultImage
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|static
class|class
name|ColorFilter
extends|extends
name|RGBImageFilter
block|{
specifier|private
specifier|final
name|int
name|color
decl_stmt|;
name|ColorFilter
parameter_list|(
name|int
name|color
parameter_list|)
block|{
name|canFilterIndexColorModel
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|color
operator|=
name|color
expr_stmt|;
block|}
specifier|public
name|int
name|filterRGB
parameter_list|(
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|,
name|int
name|argb
parameter_list|)
block|{
name|int
name|alpha
init|=
operator|(
name|argb
operator|>>
literal|24
operator|)
operator|&
literal|0xFF
decl_stmt|;
return|return
operator|(
name|alpha
operator|<<
literal|24
operator|)
operator||
name|color
return|;
block|}
block|}
specifier|static
class|class
name|SelectionFilter
extends|extends
name|ColorFilter
block|{
name|SelectionFilter
parameter_list|()
block|{
name|super
argument_list|(
name|UIManager
operator|.
name|getColor
argument_list|(
literal|"Tree.selectionForeground"
argument_list|)
operator|.
name|getRGB
argument_list|()
operator|&
literal|0x00FFFFFF
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|DisabledFilter
extends|extends
name|RGBImageFilter
block|{
name|DisabledFilter
parameter_list|()
block|{
name|canFilterIndexColorModel
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|int
name|filterRGB
parameter_list|(
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|,
name|int
name|rgb
parameter_list|)
block|{
comment|// find the average of red, green, and blue
name|float
name|avg
init|=
operator|(
operator|(
operator|(
name|rgb
operator|>>
literal|16
operator|)
operator|&
literal|0xff
operator|)
operator|/
literal|255f
operator|+
operator|(
operator|(
name|rgb
operator|>>
literal|8
operator|)
operator|&
literal|0xff
operator|)
operator|/
literal|255f
operator|+
operator|(
name|rgb
operator|&
literal|0xff
operator|)
operator|/
literal|255f
operator|)
operator|/
literal|3
decl_stmt|;
comment|// pull out the alpha channel
name|float
name|alpha
init|=
operator|(
operator|(
name|rgb
operator|>>
literal|24
operator|)
operator|&
literal|0xff
operator|)
operator|/
literal|255f
decl_stmt|;
comment|// calc the average
name|avg
operator|=
name|Math
operator|.
name|min
argument_list|(
literal|1.0f
argument_list|,
operator|(
literal|1f
operator|-
name|avg
operator|)
operator|/
operator|(
literal|100.0f
operator|/
literal|35.0f
operator|)
operator|+
name|avg
argument_list|)
expr_stmt|;
comment|// turn back into argb
return|return
operator|(
name|int
operator|)
operator|(
name|alpha
operator|*
literal|120f
operator|)
operator|<<
literal|24
operator||
operator|(
name|int
operator|)
operator|(
name|avg
operator|*
literal|255f
operator|)
operator|<<
literal|16
operator||
operator|(
name|int
operator|)
operator|(
name|avg
operator|*
literal|255f
operator|)
operator|<<
literal|8
operator||
operator|(
name|int
operator|)
operator|(
name|avg
operator|*
literal|255f
operator|)
return|;
block|}
block|}
block|}
end_class

end_unit

