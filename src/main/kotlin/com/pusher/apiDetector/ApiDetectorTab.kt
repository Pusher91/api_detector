package com.pusher.apiDetector

import burp.api.montoya.MontoyaApi
import burp.api.montoya.proxy.http.InterceptedResponse
import burp.api.montoya.ui.editor.Editor
import burp.api.montoya.ui.editor.EditorOptions
import burp.api.montoya.ui.editor.HttpRequestEditor
import burp.api.montoya.ui.editor.HttpResponseEditor
import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableRowSorter
import java.awt.*

class ApiDetectorTab(private val api: MontoyaApi) : JPanel() {
    // Create the table model and list of intercepted responses as instance variables
    private val tableModel = DefaultTableModel(arrayOf("Method", "URL", "Status", "Length"), 0)
    private val interceptedResponses = mutableListOf<InterceptedResponse>()

    init {
        layout = BorderLayout()

        // Split pane to hold table and detail view
        val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        splitPane.resizeWeight = 0.5  // This is an initial position of divider.

        // Table on the left
        val table = JTable(tableModel)
        val scrollPaneTable = JScrollPane(table)
        splitPane.leftComponent = scrollPaneTable

        // Enable table sorting
        val sorter = TableRowSorter<DefaultTableModel>(tableModel)
        table.rowSorter = sorter

        // Panel on the right for detail view, split into request and response editors
        val detailPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)

        // Create instances of HttpRequestEditor and HttpResponseEditor
        val requestEditor = api.userInterface().createHttpRequestEditor(EditorOptions.READ_ONLY)
        val responseEditor = api.userInterface().createHttpResponseEditor(EditorOptions.READ_ONLY)


        // Add the editor components to the detail pane
        detailPane.leftComponent = JScrollPane(requestEditor.uiComponent())
        detailPane.rightComponent = JScrollPane(responseEditor.uiComponent())
        splitPane.rightComponent = detailPane

        add(splitPane, BorderLayout.CENTER)

        // Add action listener to update the editors when a row is selected
        table.selectionModel.addListSelectionListener {
            val selectedRow = table.selectedRow
            if (selectedRow >= 0 && selectedRow < interceptedResponses.size) {
                val interceptedResponse = interceptedResponses[table.convertRowIndexToModel(selectedRow)]

                // Assuming interceptedResponse.initiatingRequest().raw() and interceptedResponse.raw()
                // return byte arrays of the raw HTTP request and response
                requestEditor.request = interceptedResponse.initiatingRequest()
                responseEditor.response = interceptedResponse
            }
        }
    }

    // Add a row to the table and intercepted response to the list
    fun addInterceptedResponse(interceptedResponse: InterceptedResponse, row: Array<Any>) {
        // Make sure this runs on the Event Dispatch Thread
        SwingUtilities.invokeLater {
            interceptedResponses.add(interceptedResponse)
            tableModel.addRow(row)
        }
    }
}
