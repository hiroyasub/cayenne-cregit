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
name|swing
operator|.
name|components
operator|.
name|textpane
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
name|modeler
operator|.
name|Main
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
name|modeler
operator|.
name|util
operator|.
name|ModelerUtil
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
name|swing
operator|.
name|components
operator|.
name|textpane
operator|.
name|syntax
operator|.
name|SQLSyntaxConstants
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
name|swing
operator|.
name|components
operator|.
name|textpane
operator|.
name|syntax
operator|.
name|SyntaxConstant
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BorderFactory
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
name|JScrollPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JViewport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|DocumentEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|DocumentListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|BadLocationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|Highlighter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|JTextComponent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Cursor
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

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Point
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Rectangle2D
import|;
end_import

begin_class
specifier|public
class|class
name|JCayenneTextPane
extends|extends
name|JPanel
block|{
specifier|protected
name|Highlighter
operator|.
name|HighlightPainter
name|painter
decl_stmt|;
specifier|private
name|JTextPaneScrollable
name|pane
decl_stmt|;
specifier|private
name|JScrollPane
name|scrollPane
decl_stmt|;
specifier|private
name|boolean
name|imageError
decl_stmt|;
specifier|private
name|String
name|tooltipTextError
decl_stmt|;
specifier|private
name|int
name|startYPositionToolTip
decl_stmt|;
specifier|private
name|int
name|endYPositionToolTip
decl_stmt|;
specifier|public
name|JScrollPane
name|getScrollPane
parameter_list|()
block|{
return|return
name|scrollPane
return|;
block|}
specifier|public
name|String
name|getTooltipTextError
parameter_list|()
block|{
return|return
name|tooltipTextError
return|;
block|}
specifier|public
name|void
name|setTooltipTextError
parameter_list|(
name|String
name|tooltipTextError
parameter_list|)
block|{
name|this
operator|.
name|tooltipTextError
operator|=
name|tooltipTextError
expr_stmt|;
block|}
specifier|private
specifier|static
name|Logger
name|logObj
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Main
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|pane
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|pane
operator|.
name|getText
argument_list|()
return|;
block|}
specifier|public
name|JTextComponent
name|getPane
parameter_list|()
block|{
return|return
name|pane
return|;
block|}
specifier|public
name|int
name|getStartPositionInDocument
parameter_list|()
block|{
return|return
name|pane
operator|.
name|viewToModel2D
argument_list|(
name|scrollPane
operator|.
name|getViewport
argument_list|()
operator|.
name|getViewPosition
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|int
name|getEndPositionInDocument
parameter_list|()
block|{
return|return
name|pane
operator|.
name|viewToModel2D
argument_list|(
operator|new
name|Point
argument_list|(
name|scrollPane
operator|.
name|getViewport
argument_list|()
operator|.
name|getViewPosition
argument_list|()
operator|.
name|x
operator|+
name|pane
operator|.
name|getWidth
argument_list|()
argument_list|,
name|scrollPane
operator|.
name|getViewport
argument_list|()
operator|.
name|getViewPosition
argument_list|()
operator|.
name|y
operator|+
name|pane
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|repaintPane
parameter_list|()
block|{
name|pane
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
comment|/**      * Return an int containing the wrapped line index at the given position      *       * @param pos int      * @return int      */
specifier|public
name|int
name|getLineNumber
parameter_list|(
name|int
name|pos
parameter_list|)
block|{
name|int
name|posLine
decl_stmt|;
name|int
name|y
init|=
literal|0
decl_stmt|;
try|try
block|{
name|Rectangle2D
name|caretCoords
init|=
name|pane
operator|.
name|modelToView2D
argument_list|(
name|pos
argument_list|)
decl_stmt|;
name|y
operator|=
operator|(
name|int
operator|)
name|caretCoords
operator|.
name|getY
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error: "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|int
name|lineHeight
init|=
name|pane
operator|.
name|getFontMetrics
argument_list|(
name|pane
operator|.
name|getFont
argument_list|()
argument_list|)
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|posLine
operator|=
operator|(
name|y
operator|/
name|lineHeight
operator|)
operator|+
literal|1
expr_stmt|;
return|return
name|posLine
return|;
block|}
comment|/**      * Return an int position at the given line number and symbol position in this line      *       * @param posInLine int      * @param line int      * @return int      * @throws BadLocationException      */
specifier|public
name|int
name|getPosition
parameter_list|(
name|int
name|line
parameter_list|,
name|int
name|posInLine
parameter_list|)
throws|throws
name|BadLocationException
block|{
comment|// translate lines to offsets
name|int
name|position
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|numrows
init|=
literal|1
decl_stmt|;
name|char
index|[]
name|chararr
init|=
name|pane
operator|.
name|getText
argument_list|()
operator|.
name|toCharArray
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
name|chararr
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|chararr
index|[
name|i
index|]
operator|==
literal|'\n'
condition|)
block|{
name|numrows
operator|++
expr_stmt|;
if|if
condition|(
name|numrows
operator|==
name|line
condition|)
block|{
name|position
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|position
operator|+
name|posInLine
return|;
block|}
specifier|public
name|JCayenneTextPane
parameter_list|(
name|SyntaxConstant
name|syntaxConstant
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|Dimension
name|dimension
init|=
operator|new
name|Dimension
argument_list|(
literal|15
argument_list|,
literal|15
argument_list|)
decl_stmt|;
name|setMinimumSize
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
name|setPreferredSize
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
name|setMinimumSize
argument_list|(
name|dimension
argument_list|)
expr_stmt|;
name|setBorder
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|pane
operator|=
operator|new
name|JTextPaneScrollable
argument_list|(
operator|new
name|EditorKit
argument_list|(
name|syntaxConstant
argument_list|)
argument_list|)
block|{
specifier|public
name|void
name|paint
parameter_list|(
name|Graphics
name|g
parameter_list|)
block|{
name|super
operator|.
name|paint
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|JCayenneTextPane
operator|.
name|this
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|pane
operator|.
name|setFont
argument_list|(
name|SQLSyntaxConstants
operator|.
name|DEFAULT_FONT
argument_list|)
expr_stmt|;
name|pane
operator|.
name|setBorder
argument_list|(
operator|new
name|LineNumberedBorder
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|scrollPane
operator|=
operator|new
name|JScrollPane
argument_list|(
name|pane
argument_list|)
expr_stmt|;
name|scrollPane
operator|.
name|setBorder
argument_list|(
name|BorderFactory
operator|.
name|createLineBorder
argument_list|(
name|Color
operator|.
name|LIGHT_GRAY
argument_list|)
argument_list|)
expr_stmt|;
name|painter
operator|=
operator|new
name|UnderlineHighlighterForText
operator|.
name|UnderlineHighlightPainter
argument_list|(
name|Color
operator|.
name|red
argument_list|)
expr_stmt|;
name|pane
operator|.
name|getDocument
argument_list|()
operator|.
name|addDocumentListener
argument_list|(
operator|new
name|DocumentListener
argument_list|()
block|{
specifier|public
name|void
name|insertUpdate
parameter_list|(
name|DocumentEvent
name|evt
parameter_list|)
block|{
try|try
block|{
name|String
name|text
init|=
name|pane
operator|.
name|getText
argument_list|(
name|evt
operator|.
name|getOffset
argument_list|()
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
operator|||
name|text
operator|.
name|equals
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
name|removeHighlightText
argument_list|()
expr_stmt|;
name|pane
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|text
operator|.
name|equals
argument_list|(
literal|" "
argument_list|)
operator|||
name|text
operator|.
name|equals
argument_list|(
literal|"\t"
argument_list|)
operator|||
name|text
operator|.
name|equals
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|pane
operator|.
name|repaint
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeUpdate
parameter_list|(
name|DocumentEvent
name|evt
parameter_list|)
block|{
block|}
specifier|public
name|void
name|changedUpdate
parameter_list|(
name|DocumentEvent
name|evt
parameter_list|)
block|{
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setHighlightText
parameter_list|(
name|int
name|lastIndex
parameter_list|,
name|int
name|endIndex
parameter_list|)
throws|throws
name|BadLocationException
block|{
name|Highlighter
name|highlighter
init|=
name|pane
operator|.
name|getHighlighter
argument_list|()
decl_stmt|;
name|removeHighlightText
argument_list|(
name|highlighter
argument_list|)
expr_stmt|;
name|highlighter
operator|.
name|addHighlight
argument_list|(
name|lastIndex
argument_list|,
name|endIndex
argument_list|,
name|painter
argument_list|)
expr_stmt|;
block|}
comment|/**      * set underlines text in JCayenneTextPane      *       * @param line int - starting line for underlined text      * @param lastIndex int - starting position in line for underlined text      * @param size int      * @param message String - text for toolTip, contains the text of the error      */
specifier|public
name|void
name|setHighlightText
parameter_list|(
name|int
name|line
parameter_list|,
name|int
name|lastIndex
parameter_list|,
name|int
name|size
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|Highlighter
name|highlighter
init|=
name|pane
operator|.
name|getHighlighter
argument_list|()
decl_stmt|;
name|removeHighlightText
argument_list|(
name|highlighter
argument_list|)
expr_stmt|;
if|if
condition|(
name|getText
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|int
name|position
init|=
name|getPosition
argument_list|(
name|line
argument_list|,
name|lastIndex
argument_list|)
decl_stmt|;
name|int
name|positionEnd
init|=
name|position
operator|+
name|size
decl_stmt|;
name|highlighter
operator|.
name|addHighlight
argument_list|(
name|position
argument_list|,
name|positionEnd
argument_list|,
name|painter
argument_list|)
expr_stmt|;
name|setToolTipPosition
argument_list|(
name|line
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|repaintPane
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|setToolTipPosition
argument_list|(
literal|0
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|removeHighlightText
parameter_list|(
name|Highlighter
name|highlighter
parameter_list|)
block|{
name|Highlighter
operator|.
name|Highlight
index|[]
name|highlights
init|=
name|highlighter
operator|.
name|getHighlights
argument_list|()
decl_stmt|;
for|for
control|(
name|Highlighter
operator|.
name|Highlight
name|h
range|:
name|highlights
control|)
block|{
if|if
condition|(
name|h
operator|.
name|getPainter
argument_list|()
operator|instanceof
name|UnderlineHighlighterForText
operator|.
name|UnderlineHighlightPainter
condition|)
block|{
name|highlighter
operator|.
name|removeHighlight
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setToolTipPosition
parameter_list|(
name|int
name|line
parameter_list|,
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|line
operator|!=
literal|0
condition|)
block|{
name|int
name|height
init|=
name|pane
operator|.
name|getFontMetrics
argument_list|(
name|pane
operator|.
name|getFont
argument_list|()
argument_list|)
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|start
init|=
operator|(
name|line
operator|-
literal|1
operator|)
operator|*
name|height
decl_stmt|;
name|this
operator|.
name|endYPositionToolTip
operator|=
name|start
expr_stmt|;
name|this
operator|.
name|startYPositionToolTip
operator|=
name|start
operator|+
name|height
expr_stmt|;
name|setTooltipTextError
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|imageError
operator|=
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|setToolTipText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|endYPositionToolTip
operator|=
operator|-
literal|1
expr_stmt|;
name|this
operator|.
name|startYPositionToolTip
operator|=
operator|-
literal|1
expr_stmt|;
name|setTooltipTextError
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|setToolTipText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|imageError
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getToolTipText
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getPoint
argument_list|()
operator|.
name|y
operator|>
name|endYPositionToolTip
operator|&&
name|e
operator|.
name|getPoint
argument_list|()
operator|.
name|y
operator|<
name|startYPositionToolTip
operator|&&
name|imageError
condition|)
block|{
name|setCursor
argument_list|(
name|Cursor
operator|.
name|getPredefinedCursor
argument_list|(
name|Cursor
operator|.
name|HAND_CURSOR
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|htmlText
init|=
name|getTooltipTextError
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"<br>&nbsp;"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\t"
argument_list|,
literal|"&nbsp;"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\r"
argument_list|,
literal|"<br>&nbsp;"
argument_list|)
decl_stmt|;
return|return
literal|"<HTML>"
operator|+
literal|"<body bgcolor='#FFEBCD' text='black'>"
operator|+
name|htmlText
operator|+
literal|"</body>"
return|;
block|}
else|else
block|{
name|setCursor
argument_list|(
name|Cursor
operator|.
name|getDefaultCursor
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|removeHighlightText
parameter_list|()
block|{
name|imageError
operator|=
literal|false
expr_stmt|;
name|Highlighter
name|highlighter
init|=
name|pane
operator|.
name|getHighlighter
argument_list|()
decl_stmt|;
name|removeHighlightText
argument_list|(
name|highlighter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getCaretPosition
parameter_list|()
block|{
return|return
name|pane
operator|.
name|getCaretPosition
argument_list|()
return|;
block|}
specifier|public
name|void
name|paint
parameter_list|(
name|Graphics
name|g
parameter_list|)
block|{
name|super
operator|.
name|paint
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|int
name|start
init|=
name|getStartPositionInDocument
argument_list|()
decl_stmt|;
name|int
name|end
init|=
name|getEndPositionInDocument
argument_list|()
decl_stmt|;
comment|// end pos in doc
comment|// translate offsets to lines
name|Document
name|doc
init|=
name|pane
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|int
name|startline
init|=
name|doc
operator|.
name|getDefaultRootElement
argument_list|()
operator|.
name|getElementIndex
argument_list|(
name|start
argument_list|)
operator|+
literal|1
decl_stmt|;
name|int
name|endline
init|=
name|doc
operator|.
name|getDefaultRootElement
argument_list|()
operator|.
name|getElementIndex
argument_list|(
name|end
argument_list|)
operator|+
literal|1
decl_stmt|;
name|int
name|fontHeight
init|=
name|g
operator|.
name|getFontMetrics
argument_list|(
name|pane
operator|.
name|getFont
argument_list|()
argument_list|)
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|fontDesc
init|=
name|g
operator|.
name|getFontMetrics
argument_list|(
name|pane
operator|.
name|getFont
argument_list|()
argument_list|)
operator|.
name|getDescent
argument_list|()
decl_stmt|;
name|int
name|starting_y
init|=
operator|-
literal|1
decl_stmt|;
try|try
block|{
if|if
condition|(
name|pane
operator|.
name|modelToView2D
argument_list|(
name|start
argument_list|)
operator|==
literal|null
condition|)
block|{
name|starting_y
operator|=
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
name|starting_y
operator|=
operator|(
name|int
operator|)
name|pane
operator|.
name|modelToView2D
argument_list|(
name|start
argument_list|)
operator|.
name|getY
argument_list|()
operator|-
name|scrollPane
operator|.
name|getViewport
argument_list|()
operator|.
name|getViewPosition
argument_list|()
operator|.
name|y
operator|+
name|fontHeight
operator|-
name|fontDesc
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error: "
argument_list|,
name|e1
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|line
init|=
name|startline
init|,
name|y
init|=
name|starting_y
init|;
name|line
operator|<=
name|endline
condition|;
name|y
operator|+=
name|fontHeight
operator|,
name|line
operator|++
control|)
block|{
name|Color
name|color
init|=
name|g
operator|.
name|getColor
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|-
literal|1
operator|==
name|doc
operator|.
name|getDefaultRootElement
argument_list|()
operator|.
name|getElementIndex
argument_list|(
name|pane
operator|.
name|getCaretPosition
argument_list|()
argument_list|)
condition|)
block|{
name|g
operator|.
name|setColor
argument_list|(
operator|new
name|Color
argument_list|(
literal|224
argument_list|,
literal|224
argument_list|,
literal|255
argument_list|)
argument_list|)
expr_stmt|;
name|g
operator|.
name|fillRect
argument_list|(
literal|0
argument_list|,
name|y
operator|-
name|fontHeight
operator|+
literal|3
argument_list|,
literal|30
argument_list|,
name|fontHeight
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|imageError
condition|)
block|{
name|Image
name|img
init|=
name|ModelerUtil
operator|.
name|buildIcon
argument_list|(
literal|"icon-error.png"
argument_list|)
operator|.
name|getImage
argument_list|()
decl_stmt|;
name|g
operator|.
name|drawImage
argument_list|(
name|img
argument_list|,
literal|0
argument_list|,
name|endYPositionToolTip
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|g
operator|.
name|setColor
argument_list|(
name|color
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Document
name|getDocument
parameter_list|()
block|{
return|return
name|pane
operator|.
name|getDocument
argument_list|()
return|;
block|}
specifier|public
name|void
name|setDocumentTextDirect
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|Document
name|document
init|=
name|getDocument
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|document
operator|.
name|getText
argument_list|(
literal|0
argument_list|,
name|document
operator|.
name|getLength
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
name|document
operator|.
name|remove
argument_list|(
literal|0
argument_list|,
name|document
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|insertString
argument_list|(
literal|0
argument_list|,
name|text
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error reading document"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getDocumentTextDirect
parameter_list|()
block|{
name|Document
name|document
init|=
name|getDocument
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|document
operator|.
name|getText
argument_list|(
literal|0
argument_list|,
name|document
operator|.
name|getLength
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|ex
parameter_list|)
block|{
name|logObj
operator|.
name|warn
argument_list|(
literal|"Error reading document"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
class|class
name|JTextPaneScrollable
extends|extends
name|JTextPane
block|{
name|JTextPaneScrollable
parameter_list|(
name|EditorKit
name|editorKit
parameter_list|)
block|{
comment|// Set editor kit
name|this
operator|.
name|setEditorKitForContentType
argument_list|(
name|editorKit
operator|.
name|getContentType
argument_list|()
argument_list|,
name|editorKit
argument_list|)
expr_stmt|;
name|this
operator|.
name|setContentType
argument_list|(
name|editorKit
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|getScrollableTracksViewportWidth
parameter_list|()
block|{
if|if
condition|(
name|getParent
argument_list|()
operator|instanceof
name|JViewport
condition|)
block|{
name|JViewport
name|port
init|=
operator|(
name|JViewport
operator|)
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|.
name|getWidth
argument_list|()
operator|>
name|getUI
argument_list|()
operator|.
name|getPreferredSize
argument_list|(
name|this
argument_list|)
operator|.
name|width
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
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
block|}
block|}
block|}
end_class

end_unit

