package com.pusher.apiDetector

import burp.api.montoya.MontoyaApi
import burp.api.montoya.proxy.http.InterceptedResponse
import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import javax.swing.table.TableRowSorter

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

        // Panel on the right for detail view, split into request and response text areas
        val detailPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        val requestTextArea = JTextArea()
        val responseTextArea = JTextArea()
        detailPane.leftComponent = JScrollPane(requestTextArea)
        detailPane.rightComponent = JScrollPane(responseTextArea)
        splitPane.rightComponent = detailPane

        add(splitPane, BorderLayout.CENTER)

        // Add action listener to update text areas when row is selected
        table.selectionModel.addListSelectionListener {
            val selectedRow = table.selectedRow
            if (selectedRow >= 0 && selectedRow < interceptedResponses.size) {
                val interceptedResponse = interceptedResponses[table.convertRowIndexToModel(selectedRow)]
                requestTextArea.text = interceptedResponse.initiatingRequest().toString()
                responseTextArea.text = interceptedResponse.toString()
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
