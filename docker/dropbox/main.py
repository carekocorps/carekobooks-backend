from dropbox import Dropbox
from dropbox.files import FileMetadata
from pathlib import Path
from dotenv import load_dotenv
from os import getenv

load_dotenv()

class DropboxCredentials:
    def __init__(self, access_token: str):
        self.access_token = access_token

class DropboxFileDownloader:
    def __init__(self, credentials: DropboxCredentials):
        self.__dbx = Dropbox(credentials.access_token)

    @property
    def file_names(self) -> list[str]:
        files = self.__dbx.files_list_folder(path = '')
        return [entry.name for entry in files.entries if isinstance(entry, FileMetadata)]

    def download(self, path: str) -> None:
        dir_path = Path(path)
        dir_path.mkdir(parents = True, exist_ok = True)

        for file_name in self.file_names:
            _, result = self.__dbx.files_download(path = f'/{file_name}')
            with open(dir_path / file_name, 'wb') as file:
                file.write(result.content)

def main() -> None:
    credentials = DropboxCredentials(getenv('DROPBOX_ACCESS_TOKEN'))
    file_downloader = DropboxFileDownloader(credentials)
    file_downloader.download('data')

if __name__ == '__main__':
    main()
