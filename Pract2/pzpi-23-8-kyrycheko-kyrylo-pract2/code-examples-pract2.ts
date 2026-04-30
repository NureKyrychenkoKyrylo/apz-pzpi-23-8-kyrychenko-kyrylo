import * as vscode from 'vscode';

export function activate(context: vscode.ExtensionContext) {
  const disposable = vscode.commands.registerCommand('demo.sayHello', () => {
    vscode.window.showInformationMessage('Hello from architecture demo extension!');
  });

  context.subscriptions.push(disposable);
}

export function deactivate() {}
