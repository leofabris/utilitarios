package view;

import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Leonardo F. Fabris
 * @version v1.0.0
 */
public class Main extends javax.swing.JFrame {

    //Criação das variáveis
    String diretorio;
    URL url;
    URLConnection urlConn;
    File arquivoLocal;
    File arquivoDownload;
    // É bom utilizar um buffer ao invés de gravar byte a byte.
    // Isso faz o download ser mais rápido
    // pode definir como 1 para testar
    int tamanhoBuffer = 1024;
    byte buffer[] = new byte[tamanhoBuffer];
    int tamanho;

    Thread t2 = new Thread() {
        @Override
        public void run() {

            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btnDownload.setEnabled(false);
            txtURL.setEditable(false);
            btnDownload.setText("Aguarde...");

            //Este trecho atualiza a barra de progresso
            Runnable runner = () -> {
                barraProgresso.setValue(barraProgresso.getValue() + tamanhoBuffer);
                barraProgresso.setStringPainted(true);
            };

            try {
                labelAcao.setText("Verificando URL...");

                InputStream is;
                FileOutputStream fos;
                int bytes = 0;

                //Pega a URL e separa o nome do arquivo com a extensão e instancia um novo File
                String strUrl = txtURL.getText();
                String nomeArquivo[] = strUrl.split("/");
                String nomeComExtensao = nomeArquivo[(nomeArquivo.length - 1)];
                arquivoLocal = new File(diretorio.concat(File.separator).concat(nomeComExtensao));

                labelAcao.setText("Baixando: " + nomeComExtensao);
                System.out.println("Baixando: " + nomeComExtensao);

                //Cria a URL
                url = new URL(strUrl);
                //Abre a conexão
                urlConn = url.openConnection();
                //Pega o tamanho do arquivo
                tamanho = urlConn.getContentLength();
                //Define a barra de progresso pra 0 e o máximo dela para o tamanho do arquivo
                barraProgresso.setValue(0);
                barraProgresso.setMaximum(tamanho);
                //Cria um InputStream pela URL que abrimos a conexão
                is = urlConn.getInputStream();
                //Define onde vai salvar pelo FileOutputStream
                fos = new FileOutputStream(arquivoLocal);
                // Vai gravando os bytes do arquivo de acordo com o tamanho do buffer informado 
                // lá em cima e chama o método pra atualizar a barra de progresso
                while ((bytes = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytes);
                    SwingUtilities.invokeAndWait(runner);
                }
                // Fecha o arquivo
                fos.close();
                // Fecha o InpuStream finalizando a conexão
                is.close();

                // Definie o valor da barra de tarefas pra 100% do progresso
                barraProgresso.setValue(tamanho);

                btnDownload.setEnabled(true);
                btnDownload.setText("Download");
                txtURL.setText("");
                txtURL.setEnabled(true);
                txtURL.setEditable(true);
                txtURL.requestFocus();
                labelAcao.setText("");
                
                barraProgresso.setValue(0);
                
                JOptionPane.showMessageDialog(null, "Download concluído!");
                
                
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro:\n" + ex.getMessage() + "\nVerifique o console para visualizar o StackTrace", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    };

    public Main() {
        initComponents();
        labelAcao.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtURL = new javax.swing.JTextField();
        btnDownload = new javax.swing.JButton();
        barraProgresso = new javax.swing.JProgressBar();
        labelAcao = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Download de Arquivos");
        setResizable(false);

        jLabel1.setText("URL:");

        btnDownload.setText("Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });

        labelAcao.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barraProgresso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtURL, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDownload, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
                    .addComponent(labelAcao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDownload))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelAcao)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadActionPerformed
        if (txtURL.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "URL para download não informada", "Atenção", JOptionPane.WARNING_MESSAGE);
        } else {
            JFileChooser seleciona = new JFileChooser();
            seleciona.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            seleciona.setDialogTitle("Selecione onde salvar o arquivo");
            seleciona.showOpenDialog(getParent());
            try {
                //Pega o diretório e inicia o download
                diretorio = seleciona.getSelectedFile().getAbsolutePath();
                t2.start();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Necessário selecionar onde salvar o arquivo!", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        }


    }//GEN-LAST:event_btnDownloadActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgresso;
    private javax.swing.JButton btnDownload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelAcao;
    private javax.swing.JTextField txtURL;
    // End of variables declaration//GEN-END:variables
}
